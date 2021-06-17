import io.github.reactivecircus.streamlined.FlavorDimensions
import io.github.reactivecircus.streamlined.Libraries
import io.github.reactivecircus.streamlined.ProductFlavors
import io.github.reactivecircus.streamlined.addBuildConfigField
import io.github.reactivecircus.streamlined.addResValue
import io.github.reactivecircus.streamlined.dsl.devImplementation
import io.github.reactivecircus.streamlined.dsl.mockImplementation
import io.github.reactivecircus.streamlined.dsl.prodImplementation
import io.github.reactivecircus.streamlined.envOrProp
import io.github.reactivecircus.streamlined.isCiBuild
import java.time.Instant
import org.gradle.language.nativeplatform.internal.BuildType

plugins {
    `streamlined-plugin`
    `core-library-desugaring`
    id("io.github.reactivecircus.app-versioning")
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("project-report")
    id("com.github.triplet.play")
    kotlin("android")
    kotlin("kapt")
}

@Suppress("UnstableApiUsage")
val enableAppVersioning = providers
    .environmentVariable("ENABLE_APP_VERSIONING")
    .forUseAtConfigurationTime()
    .getOrElse("true").toBoolean()

appVersioning {
    enabled.set(enableAppVersioning)
    overrideVersionCode { _, _, _ ->
        Instant.now().epochSecond.toInt()
    }
    overrideVersionName { gitTag, _, _ ->
        "${gitTag.rawTagName} (${gitTag.commitHash})"
    }
}

hilt {
    enableAggregatingTask = true
}

android {
    buildFeatures {
        buildConfig = true
        viewBinding = true
        resValues = true
    }

    defaultConfig {
        applicationId = "io.github.reactivecircus.streamlined"
        base.archivesName.set("streamlined")

        testApplicationId = "io.github.reactivecircus.streamlined.test"
        testInstrumentationRunner = "io.github.reactivecircus.streamlined.testing.ScreenTestRunner"
    }

    signingConfigs {
        named(BuildType.DEBUG.name) {
            storeFile = rootProject.file("secrets/debug.keystore")
            storePassword = "streamlined-debug"
            keyAlias = "streamlined-key"
            keyPassword = "streamlined-debug"
        }
        register(BuildType.RELEASE.name) {
            storeFile = rootProject.file("secrets/streamlined.jks")
            storePassword = envOrProp("STREAMLINED_STORE_PASSWORD")
            keyAlias = "streamlined"
            keyPassword = envOrProp("STREAMLINED_KEY_PASSWORD")
        }
    }

    bundle {
        // only support English for now
        language.enableSplit = false
    }

    buildTypes {
        named(BuildType.DEBUG.name) {
            signingConfig = signingConfigs.getByName(BuildType.DEBUG.name)
        }
        named(BuildType.RELEASE.name) {
            if (rootProject.file("secrets/streamlined.jks").exists()) {
                signingConfig = signingConfigs.getByName(BuildType.RELEASE.name)
            }
            isMinifyEnabled = true
            proguardFiles("shrinker-rules.pro")
        }
    }

    flavorDimensions.add(FlavorDimensions.ENVIRONMENT)

    productFlavors {
        register(ProductFlavors.MOCK) {
            applicationIdSuffix = ".${ProductFlavors.MOCK}"
        }
        register(ProductFlavors.DEV) {
            applicationIdSuffix = ".${ProductFlavors.DEV}"
        }
        register(ProductFlavors.PROD) {}
    }

    sourceSets {
        // common source set for dev and prod
        named(ProductFlavors.DEV) {
            java.srcDir("src/online/java")
        }
        named(ProductFlavors.PROD) {
            java.srcDir("src/online/java")
        }
    }
}

// disable google services plugin for mock flavor
tasks.whenTaskAdded {
    if (name == "processMockDebugGoogleServices") {
        enabled = false
    }
}

@Suppress("UnstableApiUsage")
androidComponents {
    // disable mockRelease, devRelease and prodDebug build variants
    beforeVariants {
        it.enabled = it.flavorName == ProductFlavors.PROD && it.buildType == BuildType.RELEASE.name ||
            it.flavorName != ProductFlavors.PROD && it.buildType == BuildType.DEBUG.name
    }

    // disable android test for dev flavor
    val devFlavor = selector().withFlavor(FlavorDimensions.ENVIRONMENT to ProductFlavors.DEV)
    beforeVariants(devFlavor) { it.enableAndroidTest = false }

    onVariants {
        if (it.buildType == BuildType.DEBUG.name) {
            // override app name for LeakCanary
            it.addResValue(key = "leak_canary_display_activity_label", type = "string", value = "streamlined leaks")

            // turn on strict mode for non-CI debug builds
            it.addBuildConfigField(key = "ENABLE_STRICT_MODE", value = !isCiBuild)

            // concatenate build variant to app name
            it.addResValue(key = "app_name", type = "string", value = "streamlined-${it.name}")
        } else {
            // set app_name for release build
            it.addResValue(key = "app_name", type = "string", value = "streamlined.")
        }

        when (it.flavorName) {
            ProductFlavors.MOCK -> {
                it.manifestPlaceholders.put("bugsnagApiKey", "")
                it.addBuildConfigField(key = "ENABLE_BUGSNAG", value = false)
                it.addBuildConfigField(key = "ENABLE_ANALYTICS", value = false)
            }
            ProductFlavors.DEV -> {
                it.manifestPlaceholders.put("bugsnagApiKey", "\"${envOrProp("RELEASE_PROBE_BUGSNAG_API_KEY")}\"")
                it.addBuildConfigField(key = "ENABLE_BUGSNAG", value = isCiBuild)
                it.addBuildConfigField(key = "ENABLE_ANALYTICS", value = true)
                it.addBuildConfigField(key = "BASE_URL", value = "\"https://newsapi.org/v2/\"")
                it.addBuildConfigField(key = "API_KEY", value = "\"${envOrProp("NEWS_API_DEV_API_KEY")}\"")
            }
            ProductFlavors.PROD -> {
                it.manifestPlaceholders.put("bugsnagApiKey", "\"${envOrProp("STREAMLINED_BUGSNAG_PROD_API_KEY")}\"")
                it.addBuildConfigField(key = "ENABLE_BUGSNAG", value = true)
                it.addBuildConfigField(key = "ENABLE_ANALYTICS", value = true)
                it.addBuildConfigField(key = "BASE_URL", value = "\"https://newsapi.org/v2/\"")
                it.addBuildConfigField(key = "API_KEY", value = "\"${envOrProp("NEWS_API_PROD_API_KEY")}\"")
            }
        }

        // database name
        it.addBuildConfigField(key = "DATABASE_NAME", value = "\"streamlined.db\"")
    }
}

play {
    serviceAccountCredentials.set(rootProject.file("secrets/play-api.json"))
    defaultToAppBundles.set(true)
}

dependencies {
    implementation(project(":bugsnag-tree"))
    mockImplementation(project(":analytics-api-no-op"))
    devImplementation(project(":analytics-api-firebase"))
    prodImplementation(project(":analytics-api-firebase"))

    implementation(project(":navigator"))
    implementation(project(":ui-common"))
    implementation(project(":ui-home"))
    implementation(project(":ui-headlines"))
    implementation(project(":ui-reading-list"))
    implementation(project(":ui-settings"))
    implementation(project(":ui-story-details"))

    implementation(project(":domain-runtime"))
    implementation(project(":data"))
    mockImplementation(project(":remote-mock"))
    devImplementation(project(":remote-real"))
    prodImplementation(project(":remote-real"))

    implementation(project(":scheduled-tasks"))

    // Blueprint
    implementation(Libraries.blueprint.interactorCoroutines)
    implementation(Libraries.blueprint.asyncCoroutines)

    // Coroutines
    implementation(Libraries.kotlinx.coroutines.core)
    implementation(Libraries.kotlinx.coroutines.android)

    // Work manager
    implementation(Libraries.androidx.work.runtimeKtx)

    // process lifecycle
    implementation(Libraries.androidx.lifecycle.process)

    // Hilt
    implementation(Libraries.hilt.android)
    kapt(Libraries.hilt.compiler)

    // Hilt AndroidX
    implementation(Libraries.androidx.hilt.work)
    kapt(Libraries.androidx.hilt.compiler)

    // timber
    implementation(Libraries.timber)

    // Enable LeakCanary for debug builds
    debugImplementation(Libraries.leakCanary.android)
    // Fix SDK leaks
    implementation(Libraries.leakCanary.plumber)

    // Pretty view hierarchy string
    implementation(Libraries.radiography)

    // Unit tests
    testImplementation(Libraries.junit)
    testImplementation(Libraries.truth)

    // Android tests
    androidTestImplementation(project(":ui-testing-framework"))
    kaptAndroidTest(Libraries.hilt.compiler)
}

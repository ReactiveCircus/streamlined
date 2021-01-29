import io.github.reactivecircus.streamlined.FlavorDimensions
import io.github.reactivecircus.streamlined.ProductFlavors
import io.github.reactivecircus.streamlined.dsl.devImplementation
import io.github.reactivecircus.streamlined.dsl.mockImplementation
import io.github.reactivecircus.streamlined.dsl.prodImplementation
import io.github.reactivecircus.streamlined.envOrProp
import io.github.reactivecircus.streamlined.isCiBuild
import io.github.reactivecircus.streamlined.libraries
import org.gradle.language.nativeplatform.internal.BuildType
import java.time.Instant

plugins {
    `streamlined-plugin`
    `core-library-desugaring`
    id("io.github.reactivecircus.app-versioning")
    id("com.android.application")
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
    .getOrElse("true").toString().toBoolean()

appVersioning {
    enabled.set(enableAppVersioning)
    overrideVersionCode { _, _, _ ->
        Instant.now().epochSecond.toInt()
    }
    overrideVersionName { gitTag, _, _ ->
        "${gitTag.rawTagName} (${gitTag.commitHash})"
    }
}

android {
    buildFeatures {
        buildConfig = true
        viewBinding = true
        resValues = true
    }

    defaultConfig {
        applicationId = "io.github.reactivecircus.streamlined"
        base.archivesBaseName = "streamlined"

        testApplicationId = "io.github.reactivecircus.streamlined.test"
        testInstrumentationRunner = "io.github.reactivecircus.streamlined.IntegrationTestRunner"

        // only support English for now
        resConfigs("en")
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
            isShrinkResources = true
            proguardFiles("shrinker-rules.pro")
        }
    }

    flavorDimensions(FlavorDimensions.ENVIRONMENT)

    productFlavors {
        register(ProductFlavors.MOCK) {
            applicationIdSuffix = ".${ProductFlavors.MOCK}"
        }
        register(ProductFlavors.DEV) {
            applicationIdSuffix = ".${ProductFlavors.DEV}"
        }
        register(ProductFlavors.PROD) {}
    }

    // filter out mockRelease, devRelease and prodDebug builds.
    variantFilter = Action {
        flavors.forEach { flavor ->
            if (flavor.name != ProductFlavors.PROD && buildType.name == BuildType.RELEASE.name ||
                flavor.name == ProductFlavors.PROD && buildType.name == BuildType.DEBUG.name
            ) {
                ignore = true
            }
        }
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

    applicationVariants.all {
        // disable google services plugin for mock flavor
        if (flavorName == ProductFlavors.MOCK) {
            tasks.named("process${name.capitalize()}GoogleServices").configure { enabled = false }
        }
    }
}

@Suppress("UnstableApiUsage")
androidComponents {
    // disable android test for dev flavor
    val devFlavor = selector().withFlavor(FlavorDimensions.ENVIRONMENT to ProductFlavors.DEV)
    beforeAndroidTests(devFlavor) { it.enabled = false }

    onVariants {
        if (it.buildType == BuildType.DEBUG.name) {
            // override app name for LeakCanary
            it.addResValue("leak_canary_display_activity_label", "string", "streamlined leaks", null)

            // turn on strict mode for non-CI debug builds
            it.addBuildConfigField("ENABLE_STRICT_MODE", !isCiBuild, null)

            // concatenate build variant to app name
            it.addResValue("app_name", "string", "streamlined-${name}", null)
        } else {
            // set app_name for release build
            it.addResValue("app_name", "string", "streamlined.", null)
        }

        when (it.flavorName) {
            ProductFlavors.MOCK -> {
                it.manifestPlaceholders.put("bugsnagApiKey", "")
                it.addBuildConfigField("ENABLE_BUGSNAG", false, null)
                it.addBuildConfigField("ENABLE_ANALYTICS", false, null)
            }
            ProductFlavors.DEV -> {
                it.manifestPlaceholders.put("bugsnagApiKey", "\"${envOrProp("RELEASE_PROBE_BUGSNAG_API_KEY")}\"")
                it.addBuildConfigField("ENABLE_BUGSNAG", isCiBuild, null)
                it.addBuildConfigField("ENABLE_ANALYTICS", true, null)
                it.addBuildConfigField("BASE_URL", "\"https://newsapi.org/v2/\"", null)
                it.addBuildConfigField("API_KEY", "\"${envOrProp("NEWS_API_DEV_API_KEY")}\"", null)
            }
            ProductFlavors.PROD -> {
                it.manifestPlaceholders.put("bugsnagApiKey", "\"${envOrProp("STREAMLINED_BUGSNAG_PROD_API_KEY")}\"")
                it.addBuildConfigField("ENABLE_BUGSNAG", true, null)
                it.addBuildConfigField("ENABLE_ANALYTICS", true, null)
                it.addBuildConfigField("BASE_URL", "\"https://newsapi.org/v2/\"", null)
                it.addBuildConfigField("API_KEY", "\"${envOrProp("NEWS_API_PROD_API_KEY")}\"", null)
            }
        }

        // database name
        it.addBuildConfigField("DATABASE_NAME", "\"streamlined.db\"", null)
    }
}

play {
    serviceAccountCredentials = rootProject.file("secrets/play-api.json")
    defaultToAppBundles = true
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
    implementation(libraries.blueprint.interactorCoroutines)
    implementation(libraries.blueprint.asyncCoroutines)

    // Coroutines
    implementation(libraries.kotlinx.coroutines.core)
    implementation(libraries.kotlinx.coroutines.android)

    // Work manager
    implementation(libraries.androidx.work.runtimeKtx)

    // process lifecycle
    implementation(libraries.androidx.lifecycle.process)

    // Dagger
    implementation(libraries.dagger.runtime)
    kapt(libraries.dagger.compiler)

    // timber
    implementation(libraries.timber)

    // Enable LeakCanary for debug builds
    debugImplementation(libraries.leakCanary.android)
    // Fix SDK leaks
    implementation(libraries.leakCanary.plumber)

    // Pretty view hierarchy string
    implementation(libraries.radiography)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.truth)

    // Android tests
    androidTestImplementation(project(":ui-testing-framework"))
    kaptAndroidTest(libraries.dagger.compiler)
}

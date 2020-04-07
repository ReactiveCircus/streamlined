import io.github.reactivecircus.streamlined.FlavorDimensions
import io.github.reactivecircus.streamlined.ProductFlavors
import io.github.reactivecircus.streamlined.dsl.devImplementation
import io.github.reactivecircus.streamlined.dsl.mockImplementation
import io.github.reactivecircus.streamlined.dsl.prodImplementation
import io.github.reactivecircus.streamlined.envOrProp
import io.github.reactivecircus.streamlined.isCiBuild
import io.github.reactivecircus.streamlined.libraries
import org.gradle.language.nativeplatform.internal.BuildType

plugins {
    `streamlined-plugin`
    `core-library-desugaring`
    `app-versioning`
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.getkeepsafe.dexcount")
    id("project-report")
    id("com.github.triplet.play")
}

appVersioning {
    major = 1
    minor = 0
    patch = 0
    buildNumber = System.getenv("BUILD_NUMBER")?.toIntOrNull()
}

android {
    @Suppress("UnstableApiUsage")
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "io.github.reactivecircus.streamlined"
        base.archivesBaseName = "streamlined"

        testApplicationId = "io.github.reactivecircus.streamlined.test"
        testInstrumentationRunner = "io.github.reactivecircus.streamlined.IntegrationTestRunner"

        // only support English for now
        resConfigs("en")

        // app name
        resValue("string", "app_name", "streamlined.")

        // database name
        buildConfigField("String", "DATABASE_NAME", "\"streamlined.db\"")
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

    @Suppress("UnstableApiUsage")
    bundle {
        // only support English for now
        language.enableSplit = false
    }

    buildTypes {
        named(BuildType.DEBUG.name) {
            signingConfig = signingConfigs.getByName(BuildType.DEBUG.name)

            // turn on strict mode for non-CI debug builds
            buildConfigField("boolean", "ENABLE_STRICT_MODE", "Boolean.parseBoolean(\"${!isCiBuild}\")")

            // override app name for LeakCanary
            resValue("string", "leak_canary_display_activity_label", "streamlined leaks")
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
            extra.set("enableBugsnag", false)
            manifestPlaceholders = mutableMapOf("bugsnagApiKey" to "")
            buildConfigField("boolean", "ENABLE_BUGSNAG", "Boolean.parseBoolean(\"false\")")
            buildConfigField("boolean", "ENABLE_ANALYTICS", "Boolean.parseBoolean(\"false\")")
        }
        register(ProductFlavors.DEV) {
            applicationIdSuffix = ".${ProductFlavors.DEV}"
            extra.set("enableBugsnag", isCiBuild)
            manifestPlaceholders = mutableMapOf("bugsnagApiKey" to envOrProp("STREAMLINED_BUGSNAG_DEV_API_KEY"))
            buildConfigField("boolean", "ENABLE_BUGSNAG", "Boolean.parseBoolean(\"${isCiBuild}\")")
            buildConfigField("boolean", "ENABLE_ANALYTICS", "Boolean.parseBoolean(\"true\")")
            buildConfigField("String", "BASE_URL", "\"https://newsapi.org/v2/\"")
            buildConfigField("String", "API_KEY", "\"${envOrProp("NEWS_API_DEV_API_KEY")}\"")
        }
        register(ProductFlavors.PROD) {
            manifestPlaceholders = mutableMapOf("bugsnagApiKey" to envOrProp("STREAMLINED_BUGSNAG_PROD_API_KEY"))
            buildConfigField("boolean", "ENABLE_BUGSNAG", "Boolean.parseBoolean(\"true\")")
            buildConfigField("boolean", "ENABLE_ANALYTICS", "Boolean.parseBoolean(\"true\")")
            buildConfigField("String", "BASE_URL", "\"https://newsapi.org/v2/\"")
            buildConfigField("String", "API_KEY", "\"${envOrProp("NEWS_API_PROD_API_KEY")}\"")
        }
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

    applicationVariants.all {
        // customize app name for debug builds
        if (buildType.name == BuildType.DEBUG.name) {
            // concatenate build variant to app name
            val appName = "streamlined-${name}"

            // set new app_name
            resValue("string", "app_name", appName)
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
}

play {
    serviceAccountCredentials = rootProject.file("secrets/play-api.json")
    defaultToAppBundles = true
    resolutionStrategy = "ignore"
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

    implementation(project(":domain"))
    implementation(project(":data"))
    mockImplementation(project(":remote-mock"))
    devImplementation(project(":remote-real"))
    prodImplementation(project(":remote-real"))

    implementation(project(":scheduled-tasks"))

    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)

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
    debugImplementation(libraries.leakCanary)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.mockk)
    testImplementation(libraries.truth)

    // Android tests
    androidTestImplementation(project(":ui-testing-framework"))
    kaptAndroidTest(libraries.dagger.compiler)
}

android.applicationVariants.all {
    // don't count dex methods for debug builds
    if (buildType.name == BuildType.DEBUG.name) {
        tasks.named("count${name.capitalize()}DexMethods").configure { enabled = false }
    }
    // disable google services plugin for mock flavor
    if (flavorName == ProductFlavors.MOCK) {
        tasks.named("process${name.capitalize()}GoogleServices").configure { enabled = false }
    }
}

@Suppress("UnstableApiUsage")
android.onVariants.run {
    // disable android test for dev flavor
    withFlavor(FlavorDimensions.ENVIRONMENT to ProductFlavors.DEV) {
        androidTest { enabled = false }
    }
}

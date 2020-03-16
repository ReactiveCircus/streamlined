import io.github.reactivecircus.streamlined.dsl.devImplementation
import io.github.reactivecircus.streamlined.dsl.mockImplementation
import io.github.reactivecircus.streamlined.dsl.prodImplementation
import io.github.reactivecircus.streamlined.envOrProp
import io.github.reactivecircus.streamlined.isCiBuild
import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    `core-library-desugaring`
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.getkeepsafe.dexcount")
    id("project-report")
}

android {
    @Suppress("UnstableApiUsage")
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "io.github.reactivecircus.streamlined"
        versionCode = 1
        versionName = "1.0"
        extra["archivesBaseName"] = "streamlined-$versionName"

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
        named("debug") {
            storeFile = rootProject.file("secrets/debug.keystore")
            storePassword = "streamlined-debug"
            keyAlias = "streamlined-key"
            keyPassword = "streamlined-debug"
        }
        register("release") {
            storeFile = rootProject.file("secrets/streamlined.keystore")
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
        named("debug") {
            signingConfig = signingConfigs.getByName("debug")

            // turn on strict mode for non-CI debug builds
            buildConfigField("boolean", "ENABLE_STRICT_MODE", "Boolean.parseBoolean(\"${!isCiBuild}\")")

            // override app name for LeakCanary
            resValue("string", "leak_canary_display_activity_label", "streamlined leaks")
        }
        named("release") {
            if (rootProject.file("secrets/streamlined.keystore").exists()) {
                signingConfig = signingConfigs.getByName("debug")
            }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles("shrinker-rules.pro")
        }
    }

    flavorDimensions("environment")

    productFlavors {
        register("mock") {
            applicationIdSuffix = ".mock"
            extra.set("enableBugsnag", false)
            manifestPlaceholders = mapOf("bugsnagApiKey" to "")
            buildConfigField("boolean", "ENABLE_BUGSNAG", "Boolean.parseBoolean(\"false\")")
            buildConfigField("boolean", "ENABLE_ANALYTICS", "Boolean.parseBoolean(\"false\")")
        }
        register("dev") {
            applicationIdSuffix = ".dev"
            extra.set("enableBugsnag", isCiBuild)
            manifestPlaceholders = mapOf("bugsnagApiKey" to envOrProp("STREAMLINED_BUGSNAG_DEV_API_KEY"))
            buildConfigField("boolean", "ENABLE_BUGSNAG", "Boolean.parseBoolean(\"${isCiBuild}\")")
            buildConfigField("boolean", "ENABLE_ANALYTICS", "Boolean.parseBoolean(\"true\")")
            buildConfigField("String", "BASE_URL", "\"https://newsapi.org/v2/\"")
            buildConfigField("String", "API_KEY", "\"${envOrProp("NEWS_API_DEV_API_KEY")}\"")
        }
        register("prod") {
            manifestPlaceholders = mapOf("bugsnagApiKey" to envOrProp("STREAMLINED_BUGSNAG_PROD_API_KEY"))
            buildConfigField("boolean", "ENABLE_BUGSNAG", "Boolean.parseBoolean(\"true\")")
            buildConfigField("boolean", "ENABLE_ANALYTICS", "Boolean.parseBoolean(\"true\")")
            buildConfigField("String", "BASE_URL", "\"https://newsapi.org/v2/\"")
            buildConfigField("String", "API_KEY", "\"${envOrProp("NEWS_API_PROD_API_KEY")}\"")
        }
    }

    // filter out mockRelease, devRelease and prodDebug builds.
    variantFilter = Action {
        flavors.forEach { flavor ->
            if (flavor.name != "prod" && buildType.name == "release" ||
                flavor.name == "prod" && buildType.name == "debug"
            ) {
                ignore = true
            }
        }
    }

    applicationVariants.all {
        // customize app name for debug builds
        if (buildType.name == "debug") {
            // concatenate build variant to app name
            val appName = "streamlined-${name}"

            // set new app_name
            resValue("string", "app_name", appName)
        }
    }

    sourceSets {
        // common source set for dev and prod
        named("dev") {
            java.srcDir("src/online/java")
        }
        named("prod") {
            java.srcDir("src/online/java")
        }
    }
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

tasks.configureEach {
    when {
        // don't count dex methods for debug builds
        name.matches("count(?i).+debugDexMethods".toRegex()) -> {
            onlyIf { false }
        }
        // disable google services plugin for mock flavor
        name.matches("process(?i)mock.+GoogleServices".toRegex()) -> {
            onlyIf { false }
        }
        // disable all AndroidTest tasks for prod flavor
        name.matches(".*(?i)(prod).+AndroidTest.*".toRegex()) -> {
            onlyIf { false }
        }
    }
}

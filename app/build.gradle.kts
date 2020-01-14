import io.github.reactivecircus.streamlined.envOrProp
import io.github.reactivecircus.streamlined.isCiBuild
import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
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

    // enabled new Java 8 language APIs
    compileOptions.coreLibraryDesugaringEnabled = true

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
    }

    lintOptions {
        disable("ParcelCreator")
        disable("GoogleAppIndexingWarning")
        isQuiet = false
        isIgnoreWarnings = false
        htmlReport = true
        xmlReport = true
        htmlOutput = file("$buildDir/reports/lint/lint-reports.html")
        xmlOutput = file("$buildDir/reports/lint/lint-reports.xml")
        isCheckDependencies = true
        isIgnoreTestSources = true
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
            extra.set("alwaysUpdateBuildId", false)

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
            manifestPlaceholders = mapOf("bugsnagApiKey" to envOrProp("STREAMLINED_BUGSNAG_DEV_API_KEY"))
            buildConfigField("boolean", "ENABLE_BUGSNAG", "Boolean.parseBoolean(\"true\")")
            buildConfigField("boolean", "ENABLE_ANALYTICS", "Boolean.parseBoolean(\"true\")")
        }
        register("prod") {
            manifestPlaceholders = mapOf("bugsnagApiKey" to envOrProp("STREAMLINED_BUGSNAG_PROD_API_KEY"))
            buildConfigField("boolean", "ENABLE_BUGSNAG", "Boolean.parseBoolean(\"true\")")
            buildConfigField("boolean", "ENABLE_ANALYTICS", "Boolean.parseBoolean(\"true\")")
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
        getByName("dev").java.srcDir("src/common/java")
        getByName("prod").java.srcDir("src/common/java")
    }
}

dependencies {
    implementation(project(":bugsnag-tree"))
    "mockImplementation"(project(":analytics-api-no-op"))
    "devImplementation"(project(":analytics-api-firebase"))
    "prodImplementation"(project(":analytics-api-firebase"))

    implementation(project(":router"))
    implementation(project(":ui-common"))
    implementation(project(":ui-home"))
    implementation(project(":ui-headlines"))
    implementation(project(":ui-reading-list"))
    implementation(project(":ui-settings"))
    implementation(project(":ui-story-details"))

    implementation(project(":data"))
    "prodImplementation"(project(":remote-real"))
    "mockImplementation"(project(":remote-mock"))

    implementation(project(":periodic-work"))

    // Java 8 desugaring
    "coreLibraryDesugaring"(libraries.desugarLibs)

    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)

    // Blueprint
    implementation(libraries.blueprint.threadingCoroutines)

    // Coroutines
    implementation(libraries.kotlinx.coroutines.core)
    implementation(libraries.kotlinx.coroutines.android)

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
}

tasks.configureEach {
    when {
        // don't count dex methods for debug builds
        name.matches("count(?i).+debugDexMethods".toRegex()) -> {
            enabled = false
        }
        // disable google services plugin for mock flavor
        name.matches("process(?i)mock.+GoogleServices".toRegex()) -> {
            enabled = false
        }
        // disable all AndroidTest tasks for dev and prod flavors
        name.matches(".*(?i)(dev|prod).+AndroidTest.*".toRegex()) -> {
            enabled = false
        }
    }
}

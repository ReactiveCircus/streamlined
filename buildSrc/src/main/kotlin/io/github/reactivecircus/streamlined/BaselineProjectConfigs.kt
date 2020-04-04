package io.github.reactivecircus.streamlined

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

/**
 * Configure root project.
 * Note that classpath dependencies still need to be defined in the `buildscript` block in the top-level build.gradle.kts file.
 */
internal fun Project.configureForRootProject() {
    // register task for cleaning the build directory in the root project
    tasks.register("clean", Delete::class.java) {
        delete(rootProject.buildDir)
    }
}

/**
 * Apply baseline configurations for all projects (including the root project).
 */
internal fun Project.configureForAllProjects() {
    // apply and configure detekt plugin
    configureDetektPlugin()

    repositories.apply {
        mavenCentral()
        google()
        jcenter()
    }

    tasks.withType(JavaCompile::class.java).configureEach {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    tasks.withType(KotlinJvmCompile::class.java).configureEach {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    tasks.withType(KotlinCompile::class.java).configureEach {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + additionalCompilerArgs
        }
    }

    tasks.withType(Test::class.java).configureEach {
        maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
}

/**
 * Apply baseline configurations for all Android projects (Application and Library).
 */
@Suppress("UnstableApiUsage")
internal fun TestedExtension.configureCommonAndroidOptions() {
    setCompileSdkVersion(androidSdk.compileSdk)
    buildToolsVersion(androidSdk.buildTools)

    defaultConfig.apply {
        minSdkVersion(androidSdk.minSdk)
        targetSdkVersion(androidSdk.targetSdk)

        // only support English for now
        resConfigs("en")
    }

    testOptions.animationsDisabled = true

    dexOptions.preDexLibraries = !isCiBuild

    compileOptions(Action {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    })
}

/**
 * Apply configuration options for Android Application projects.
 */
internal fun AppExtension.configureAndroidApplicationOptions(project: Project) {
    lintOptions {
        disable("ParcelCreator")
        disable("GoogleAppIndexingWarning")
        isQuiet = false
        isIgnoreWarnings = false
        htmlReport = true
        xmlReport = true
        htmlOutput = File("${project.buildDir}/reports/lint/lint-reports.html")
        xmlOutput = File("${project.buildDir}/reports/lint/lint-reports.xml")
        isCheckDependencies = true
        isIgnoreTestSources = true
    }

    packagingOptions {
        excludes = setOf(
            "META-INF/*.kotlin_module",
            "META-INF/proguard/coroutines.pro",
            "META-INF/MANIFEST.MF",
            "LICENSE.txt"
        )
    }
}

/**
 * Apply configuration options for Android Library projects.
 */
@Suppress("UnstableApiUsage")
internal fun LibraryExtension.configureAndroidLibraryOptions(project: Project) {
    // disable unit test tasks if the unitTest source set is empty
    if (!project.hasUnitTestSource) {
        onVariants {
            unitTest { enabled = false }
        }
    }

    // disable android test tasks if the androidTest source set is empty
    if (!project.hasAndroidTestSource) {
        onVariants {
            androidTest { enabled = false }
        }
    }

    // Disable generating BuildConfig.java
    buildFeatures.buildConfig = false

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

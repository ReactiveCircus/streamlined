package io.github.reactivecircus.streamlined

import com.android.build.api.extension.ApplicationAndroidComponentsExtension
import com.android.build.api.extension.LibraryAndroidComponentsExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import java.io.File
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Apply baseline configurations for all projects (including the root project).
 */
internal fun Project.configureForAllProjects() {
    // apply and configure detekt plugin
    configureDetektPlugin()

    repositories {
        mavenCentral()
        google()
        jcenter()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            useIR = true
            jvmTarget = JavaVersion.VERSION_11.toString()
            freeCompilerArgs = freeCompilerArgs + additionalCompilerArgs
        }
    }

    tasks.withType<Test> {
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

    defaultConfig {
        minSdkVersion(androidSdk.minSdk)
        targetSdkVersion(androidSdk.targetSdk)

        // only support English for now
        resConfigs("en")
    }

    testOptions.animationsDisabled = true
}

/**
 * Apply configuration options for Android Application projects.
 */
@Suppress("UnstableApiUsage")
internal fun BaseAppModuleExtension.configureAndroidApplicationOptions(project: Project) {
    lint {
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
}

/**
 * Configure the Application Library Component based on build variants.
 */
@Suppress("UnstableApiUsage")
internal fun LibraryAndroidComponentsExtension.configureAndroidLibraryVariants(project: Project) {
    project.plugins.withType<KotlinAndroidPluginWrapper> {
        // disable unit test tasks if the unitTest source set is empty
        if (!project.hasUnitTestSource) {
            beforeVariants { it.enableUnitTest = false }
        }

        // disable android test tasks if the androidTest source set is empty
        if (!project.hasAndroidTestSource) {
            beforeVariants { it.enableAndroidTest = false }
        }
    }
}

/**
 * Configure the Application Android Component based on build variants.
 */
@Suppress("UnstableApiUsage")
internal fun ApplicationAndroidComponentsExtension.configureAndroidApplicationVariants(project: Project) {
    project.plugins.withType<KotlinAndroidPluginWrapper> {
        // disable unit test tasks if the unitTest source set is empty
        if (!project.hasUnitTestSource) {
            beforeVariants { it.enableUnitTest = false }
        }

        // disable android test tasks if the androidTest source set is empty
        if (!project.hasAndroidTestSource) {
            beforeVariants { it.enableAndroidTest = false }
        }
    }
}

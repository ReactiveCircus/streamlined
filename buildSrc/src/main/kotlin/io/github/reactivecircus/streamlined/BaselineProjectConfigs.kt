package io.github.reactivecircus.streamlined

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
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
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
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
internal fun TestedExtension.configureCommonAndroidOptions() {
    compileSdkVersion(androidSdk.compileSdk)
    buildToolsVersion(androidSdk.buildTools)

    defaultConfig {
        minSdk = androidSdk.minSdk
        targetSdk = androidSdk.targetSdk

        // only support English for now
        resourceConfigurations.add("en")
    }

    testOptions.animationsDisabled = true

    // TODO re-enable once lint analysis failure is fixed
    lintOptions.disable += "DialogFragmentCallbacksDetector"
}

/**
 * Apply configuration options for Android Application projects.
 */
internal fun BaseAppModuleExtension.configureAndroidApplicationOptions(project: Project) {
    lint {
        disable.add("ParcelCreator")
        disable.add("GoogleAppIndexingWarning")
        quiet = false
        ignoreWarnings = false
        htmlReport = true
        xmlReport = true
        htmlOutput = File("${project.buildDir}/reports/lint/lint-reports.html")
        xmlOutput = File("${project.buildDir}/reports/lint/lint-reports.xml")
        checkDependencies = true
        ignoreTestSources = true
    }
}

/**
 * Configure the Application Library Component based on build variants.
 */
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

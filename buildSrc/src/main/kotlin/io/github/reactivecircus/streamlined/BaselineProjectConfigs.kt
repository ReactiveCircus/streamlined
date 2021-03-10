package io.github.reactivecircus.streamlined

import com.android.build.api.extension.ApplicationAndroidComponentsExtension
import com.android.build.api.extension.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import java.io.File
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
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
 * Apply baseline project configurations for an Android Application project.
 */
internal fun Project.configureAndroidApplication() {
    // common android configs
    extensions.getByType<TestedExtension>().configureCommonAndroidOptions()

    // android variant configs
    extensions.getByType<ApplicationAndroidComponentsExtension>().configureAndroidApplicationVariants(project)

    // android application configs
    extensions.configure<BaseAppModuleExtension> {
        lint {
            // TODO remove once https://issuetracker.google.com/issues/162155191 is fixed.
            disable("InvalidFragmentVersionForActivityResult")
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

    // disable unit tests for some build variants if `slimTests` project property is provided
    project.configureSlimTests()
}

/**
 * Apply baseline project configurations for an Android Library project.
 */
internal fun Project.configureAndroidLibrary() {
    // common android configs
    extensions.getByType<TestedExtension>().configureCommonAndroidOptions()

    // android variant configs
    extensions.getByType<LibraryAndroidComponentsExtension>().configureAndroidLibraryVariants(project)

    // android library configs
    extensions.configure<LibraryExtension> {
        packagingOptions {
            resources {
                pickFirsts.add("META-INF/AL2.0")
                pickFirsts.add("META-INF/LGPL2.1")
            }
        }
    }

    // disable unit tests for some build variants if `slimTests` project property is provided
    project.configureSlimTests()
}

/**
 * Apply baseline configurations for both Android Application and Library projects.
 */
private fun TestedExtension.configureCommonAndroidOptions() {
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
private fun ApplicationAndroidComponentsExtension.configureAndroidApplicationVariants(project: Project) {
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

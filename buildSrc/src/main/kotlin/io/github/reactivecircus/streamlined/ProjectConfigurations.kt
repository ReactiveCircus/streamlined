package io.github.reactivecircus.streamlined

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

/**
 * Apply default configurations to project with [StreamlinedPlugin] applied.
 */
internal fun Project.configureProject() {
    // apply and configure detekt plugin for all projects
    configureDetektPlugin()

    // apply baseline configurations for all projects
    configureForAllProjects()

    // apply configurations specific to root project
    if (isRoot) {
        configureForRootProject()
    }

    // apply baseline configurations based on plugins applied
    plugins.all { plugin ->
        when (plugin) {
            is JavaPlugin,
            is JavaLibraryPlugin -> {
                project.convention.getPlugin(JavaPluginConvention::class.java).apply {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }
            }
            is LibraryPlugin -> {
                libraryExtension.configureCommonAndroidOptions()
                libraryExtension.configureAndroidLibraryOptions()
                enableSlimTests()
            }
            is AppPlugin -> {
                appExtension.configureCommonAndroidOptions()
                enableSlimTests()
            }
        }
    }
}

/**
 * Configure root project.
 * Note that classpath dependencies still need to be defined in the `buildscript` block in the top-level build.gradle.kts file.
 */
private fun Project.configureForRootProject() {
    // register task for cleaning the build directory in the root project
    tasks.register("clean", Delete::class.java) {
        delete(rootProject.buildDir)
    }
}

/**
 * Apply baseline configurations for all projects (including the root project).
 */
private fun Project.configureForAllProjects() {
    repositories.apply {
        mavenCentral()
        google()
        jcenter()
    }

    tasks.withType(JavaCompile::class.java).configureEach { task ->
        task.sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        task.targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    tasks.withType(KotlinJvmCompile::class.java).configureEach { task ->
        task.kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    tasks.withType(KotlinCompile::class.java).configureEach { task ->
        task.kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + additionalCompilerArgs
        }
    }

    tasks.withType(Test::class.java).configureEach { test ->
        test.maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
        test.testLogging {
            it.events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
}

/**
 * Apply baseline configurations for all Android projects (Application and Library).
 */
private fun BaseExtension.configureCommonAndroidOptions() {
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
        it.sourceCompatibility = JavaVersion.VERSION_1_8
        it.targetCompatibility = JavaVersion.VERSION_1_8
    })
}

/**
 * Apply configuration options for Android Library projects.
 */
@Suppress("UnstableApiUsage")
private fun LibraryExtension.configureAndroidLibraryOptions() {
    // Disable generating BuildConfig.java
    buildFeatures.buildConfig = false
}

private val Project.isRoot get() = this == this.rootProject
private val Project.appExtension: AppExtension get() = extensions.getByType(AppExtension::class.java)
private val Project.libraryExtension: LibraryExtension get() = extensions.getByType(LibraryExtension::class.java)

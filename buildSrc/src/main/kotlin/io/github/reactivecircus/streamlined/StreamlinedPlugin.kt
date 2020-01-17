@file:Suppress("unused")

package io.github.reactivecircus.streamlined

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin

/**
 * A plugin that provides baseline gradle configurations for all projects, including:
 * - root project
 * - Android Application projects
 * - Android Library projects
 * - Kotlin JVM projects
 * - Java JVM projects
 *
 * Apply this plugin to the build.gradle.kts file in all projects:
 * ```
 * plugins {
 *     id 'streamlined-plugin'
 * }
 * ```
 */
class StreamlinedPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.afterEvaluate {
            // apply common baseline configurations for all projects including the root project
            configureForAllProjects()

            // apply configurations specific to root project
            if (isRoot) {
                configureForRootProject()
            }

            // apply baseline configurations based on plugins applied
            plugins.all {
                when (this) {
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
                    is Kapt3GradleSubplugin -> {
                        configureKapt()
                    }
                }
            }
        }
    }
}

private val Project.isRoot get() = this == this.rootProject
private val Project.appExtension: AppExtension get() = extensions.getByType(AppExtension::class.java)
private val Project.libraryExtension: LibraryExtension get() = extensions.getByType(LibraryExtension::class.java)

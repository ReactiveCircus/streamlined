@file:Suppress("unused")

package io.github.reactivecircus.streamlined

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.getPlugin
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
 *     `streamlined-plugin`
 * }
 * ```
 */
class StreamlinedPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // apply common baseline configurations for all projects including the root project
        project.configureForAllProjects()

        // apply configurations specific to root project
        if (project.isRoot) {
            // register task for cleaning the build directory in the root project
            project.tasks.register("clean", Delete::class.java) {
                delete(project.rootProject.buildDir)
            }
            project.configureDependencyGraphGenerator()
        }

        // apply baseline configurations based on plugins applied
        project.plugins.all {
            when (this) {
                is JavaPlugin,
                is JavaLibraryPlugin -> {
                    project.convention.getPlugin<JavaPluginConvention>().apply {
                        sourceCompatibility = JavaVersion.VERSION_11
                        targetCompatibility = JavaVersion.VERSION_11
                    }
                }
                is LibraryPlugin -> {
                    project.configureAndroidLibrary()
                }
                is AppPlugin -> {
                    project.configureAndroidApplication()
                }
                is Kapt3GradleSubplugin -> {
                    project.configureKapt()
                }
            }
        }
    }
}

internal val Project.isRoot get() = this == this.rootProject

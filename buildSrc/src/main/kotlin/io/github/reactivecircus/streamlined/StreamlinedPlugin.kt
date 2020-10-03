@file:Suppress("unused")

package io.github.reactivecircus.streamlined

import com.android.build.api.extension.ApplicationAndroidComponentsExtension
import com.android.build.api.extension.LibraryAndroidComponentsExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.kotlin.dsl.getByType
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
            project.configureForRootProject()
        }

        // apply baseline configurations based on plugins applied
        project.plugins.all {
            when (this) {
                is JavaPlugin,
                is JavaLibraryPlugin -> {
                    project.convention.getPlugin<JavaPluginConvention>().apply {
                        sourceCompatibility = JavaVersion.VERSION_1_8
                        targetCompatibility = JavaVersion.VERSION_1_8
                    }
                }
                is LibraryPlugin -> {
                    project.extensions.getByType<TestedExtension>().configureCommonAndroidOptions()
                    project.extensions.getByType<LibraryAndroidComponentsExtension>().configureAndroidVariants(project)
                    project.configureSlimTests()
                }
                is AppPlugin -> {
                    project.extensions.getByType<TestedExtension>().configureCommonAndroidOptions()
                    project.extensions.getByType<BaseAppModuleExtension>().configureAndroidApplicationOptions(project)
                    project.extensions.getByType<ApplicationAndroidComponentsExtension>().configureAndroidVariants(project)
                    project.configureSlimTests()
                }
                is Kapt3GradleSubplugin -> {
                    project.configureKapt()
                }
            }
        }
    }
}

private val Project.isRoot get() = this == this.rootProject

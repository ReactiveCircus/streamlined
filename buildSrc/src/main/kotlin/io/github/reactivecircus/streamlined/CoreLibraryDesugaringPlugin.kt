@file:Suppress("unused")

package io.github.reactivecircus.streamlined

import com.android.build.gradle.TestedExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * A plugin that enables Java 8 desugaring for consuming new Java language APIs.
 *
 * Apply this plugin to the build.gradle.kts file in Android Application or Android Library projects:
 * ```
 * plugins {
 *     id 'core-library-desugaring'
 * }
 * ```
 */
class CoreLibraryDesugaringPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.afterEvaluate {
            val isAndroidAppProject = project.hasAndroidAppPlugin
            val isAndroidLibraryProject = project.hasAndroidLibraryPlugin

            require(isAndroidAppProject || isAndroidLibraryProject) {
                "Core library desugaring should only be enabled on Android projects but ${project.displayName} doesn't have either 'com.android.library' or 'com.android.application' plugin applied."
            }

            extensions.getByType<TestedExtension>().apply {
                compileOptions.isCoreLibraryDesugaringEnabled = true
                project.dependencies.add("coreLibraryDesugaring", libraries.desugarLibs)
            }
        }
    }
}

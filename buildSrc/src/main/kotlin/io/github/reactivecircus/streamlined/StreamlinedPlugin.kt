@file:Suppress("unused")

package io.github.reactivecircus.streamlined

import org.gradle.api.Plugin
import org.gradle.api.Project

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
        project.afterEvaluate { it.configureProject() }
    }
}

@file:Suppress(
    "unused",
    "RemoveRedundantBackticks"
)

package io.github.reactivecircus.streamlined.dsl

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * Adds a dependency to the 'mockImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 */
fun DependencyHandler.`mockImplementation`(dependencyNotation: Any): Dependency? =
    add("mockImplementation", dependencyNotation)

/**
 * Adds a dependency to the 'devImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 */
fun DependencyHandler.`devImplementation`(dependencyNotation: Any): Dependency? =
    add("devImplementation", dependencyNotation)

/**
 * Adds a dependency to the 'prodImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 */
fun DependencyHandler.`prodImplementation`(dependencyNotation: Any): Dependency? =
    add("prodImplementation", dependencyNotation)

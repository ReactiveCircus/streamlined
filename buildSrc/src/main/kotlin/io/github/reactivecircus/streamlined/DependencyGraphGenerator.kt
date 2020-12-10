package io.github.reactivecircus.streamlined

import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorExtension
import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorExtension.Generator
import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorPlugin
import guru.nidi.graphviz.attribute.Style
import guru.nidi.graphviz.engine.Format
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

/**
 * Configure project dependency graph generation.
 * Run ./gradlew generateDependencyGraph to generate the project dependency graph.
 * Generated file will be available at <rootProject>/build/reports/dependency-graph/dependency-graph.svg
 */
internal fun Project.configureDependencyGraphGenerator() {
    require(isRoot)
    pluginManager.apply(DependencyGraphGeneratorPlugin::class.java)
    plugins.withType<DependencyGraphGeneratorPlugin> {
        extensions.configure<DependencyGraphGeneratorExtension> {
            generators = listOf(
                Generator(
                    include = { dependency ->
                        // only include projects and filter out the ones that depend on itself in other configurations (e.g. test)
                        dependency.moduleGroup == rootProject.name && dependency.parents.none {
                            dependency.moduleName == it.moduleName
                        }
                    },
                    dependencyNode = { node, _ ->
                        node.add(Style.SOLID)
                    },
                    includeConfiguration = { configuration ->
                        configuration.name.contains("runtimeClasspath", ignoreCase = true)
                    },
                    outputFormats = listOf(Format.SVG)
                )
            )
        }
    }
}

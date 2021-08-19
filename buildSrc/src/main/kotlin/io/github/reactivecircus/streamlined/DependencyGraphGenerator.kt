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
 * Run `./gradlew generateDependencyGraph` to generate the project dependency graph.
 * Generated svg file will be available at ./build/reports/dependency-graph/dependency-graph.svg.
 *
 * To also generate a png file:
 * `./gradlew generateDependencyGraph -PgeneratePng`
 * Generated png file will be available at ./build/reports/dependency-graph/dependency-graph.png.
 *
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
                    outputFormats = mutableListOf(Format.SVG).apply {
                        if (providers.gradleProperty(GENERATE_PNG_PROPERTY).forUseAtConfigurationTime().isPresent) {
                            add(Format.PNG)
                        }
                    }
                )
            )
        }
    }
}

private const val GENERATE_PNG_PROPERTY = "generatePng"

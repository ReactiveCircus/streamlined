package io.github.reactivecircus.streamlined

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

/**
 * Apply detekt configs to the [Project].
 */
internal fun Project.configureDetektPlugin() {
    // apply detekt plugin
    pluginManager.apply(DetektPlugin::class.java)

    // enable Ktlint formatting
    dependencies.add("detektPlugins", "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}")

    plugins.withType<DetektPlugin> {
        extensions.configure<DetektExtension> {
            source = files("src/")
            config = files("${project.rootDir}/detekt.yml")
            buildUponDefaultConfig = true
            allRules = true
        }
        tasks.withType<Detekt>().configureEach {
            reports {
                html.outputLocation.set(file("build/reports/detekt/${project.name}.html"))
            }
        }
    }
}

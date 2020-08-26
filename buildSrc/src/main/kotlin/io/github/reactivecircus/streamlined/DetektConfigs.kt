package io.github.reactivecircus.streamlined

import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Action
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
    dependencies.add("detektPlugins", "io.gitlab.arturbosch.detekt:detekt-formatting:${versions.detekt}")

    plugins.withType<DetektPlugin> {
        extensions.configure<DetektExtension> {
            input = files("src/")
            failFast = true
            autoCorrect = true
            config = files("${project.rootDir}/detekt.yml")
            buildUponDefaultConfig = true
            reports(Action {
                reports.html.destination = file("${project.buildDir}/reports/detekt/${project.name}.html")
            })
        }
    }
}

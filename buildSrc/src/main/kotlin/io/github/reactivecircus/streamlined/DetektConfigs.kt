package io.github.reactivecircus.streamlined

import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Action
import org.gradle.api.Project

/**
 * Apply detekt configs to the [Project].
 */
internal fun Project.configureDetektPlugin() {
    // apply detekt plugin
    pluginManager.apply(DetektPlugin::class.java)

    // enable Ktlint formatting
    dependencies.add("detektPlugins", "io.gitlab.arturbosch.detekt:detekt-formatting:${versions.detekt}")

    pluginManager.withPlugin("io.gitlab.arturbosch.detekt") {
        extensions.configure(DetektExtension::class.java) {
            it.toolVersion = versions.detekt
            it.input = files("src/")
            it.failFast = true
            it.config = files("${project.rootDir}/detekt.yml")
            it.buildUponDefaultConfig = true
            it.reports(Action { reports ->
                reports.html.destination = file("${project.buildDir}/reports/detekt/${project.name}.html")
            })
        }
    }
}

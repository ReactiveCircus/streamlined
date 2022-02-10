package io.github.reactivecircus.streamlined

import org.gradle.api.Project

val Project.isCiBuild: Boolean
    get() = providers.environmentVariable("CI").orNull == "true"

fun Project.envOrProp(name: String): String {
    return providers.environmentVariable(name).orNull
        ?: providers.gradleProperty(name).getOrElse("")
}

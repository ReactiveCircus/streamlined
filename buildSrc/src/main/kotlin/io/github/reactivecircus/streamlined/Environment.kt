package io.github.reactivecircus.streamlined

import org.gradle.api.Project

val Project.isCiBuild: Boolean
    get() = providers.environmentVariable("CI").forUseAtConfigurationTime().orNull == "true"

fun Project.envOrProp(name: String): String {
    return providers.environmentVariable(name).forUseAtConfigurationTime().orNull
        ?: providers.gradleProperty(name).forUseAtConfigurationTime().getOrElse("")
}

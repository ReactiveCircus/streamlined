package io.github.reactivecircus.streamlined

import org.gradle.api.Project

@Suppress("UnstableApiUsage")
val Project.isCiBuild: Boolean
    get() = providers.environmentVariable("CI").forUseAtConfigurationTime().orNull == "true"

@Suppress("UnstableApiUsage")
fun Project.envOrProp(name: String): String {
    return providers.environmentVariable(name).forUseAtConfigurationTime().orNull
        ?: providers.gradleProperty(name).forUseAtConfigurationTime().getOrElse("")
}

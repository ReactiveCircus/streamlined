package io.github.reactivecircus.streamlined

import org.gradle.api.Project

val isCiBuild: Boolean get() = System.getenv("CI") == "true"

fun Project.envOrProp(name: String): String {
    return System.getenv(name) ?: this.properties.getOrDefault(name, "") as String
}

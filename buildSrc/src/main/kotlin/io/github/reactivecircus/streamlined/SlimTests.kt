package io.github.reactivecircus.streamlined

import org.gradle.api.Project

private const val SLIM_TESTS_PROPERTY = "slimTests"
private const val SKIPPED_TESTS_REGEX_PATTERN = "test.*(?i)(debug|mock|dev)UnitTest"

/**
 * When the "slimTests" project property is provided, skip the unit tests for `debug`, `mock` and `dev`
 * to avoid running the same tests repeatedly in different build variants.
 *
 * Example: `./gradlew test -PslimTests` will run unit tests for *ProdRelease and *Release build variants
 * in Android App and Library projects, and all tests in JVM projects.
 */
internal fun Project.enableSlimTests() {
    tasks.configureEach { task ->
        if (task.name.matches(SKIPPED_TESTS_REGEX_PATTERN.toRegex()) && hasProperty(SLIM_TESTS_PROPERTY)) {
            task.enabled = false
        }
    }
}

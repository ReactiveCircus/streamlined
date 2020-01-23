package io.github.reactivecircus.streamlined

import org.gradle.api.Project

/**
 * When the "slimTests" project property is provided, skip the unit tests and android tests
 * for `release` build type and `dev` and `mock` product flavors to avoid running the same tests repeatedly
 * in different build variants.
 *
 * Examples:
 * `./gradlew test -PslimTests` will run unit tests for `mockDebug` and `debug` build variants
 * in Android App and Library projects, and all tests in JVM projects.
 *
 * `./gradlew connectedCheck -PslimTests` will run android tests for `mockDebug` and `debug` build variants
 * in Android App and Library projects.
 */
internal fun Project.enableSlimTests() {
    if (hasProperty(SLIM_TESTS_PROPERTY)) {
        tasks.configureEach {
            if (name.matches(SKIPPED_TESTS_REGEX_PATTERN.toRegex())) {
                onlyIf { false }
            }
        }
    }
}

private const val SLIM_TESTS_PROPERTY = "slimTests"
private const val SKIPPED_TESTS_REGEX_PATTERN = "test.*(?i)(release|dev|prod)UnitTest"


package io.github.reactivecircus.streamlined

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.language.nativeplatform.internal.BuildType

/**
 * When the "slimTests" project property is provided, disable the unit test tasks
 * on `release` build type and `dev` and `prod` product flavors to avoid running the same tests repeatedly
 * in different build variants.
 *
 * Examples:
 * `./gradlew test -PslimTests` will run unit tests for `mockDebug` and `debug` build variants
 * in Android App and Library projects, and all tests in JVM projects.
 */
@Suppress("UnstableApiUsage")
internal fun Project.configureSlimTests() {
    if (hasProperty(SLIM_TESTS_PROPERTY)) {
        // disable unit test tasks on the release build type for Android Library projects
        extensions.findByType<LibraryExtension>()?.run {
            onVariants.withBuildType(BuildType.RELEASE.name) {
                unitTest { enabled = false }
            }
        }

        // disable unit test tasks on the release build type and all non-mock flavors for Android Application projects.
        extensions.findByType<BaseAppModuleExtension>()?.run {
            onVariants.withBuildType(BuildType.RELEASE.name) {
                unitTest { enabled = false }
            }
            onVariants.withFlavor(FlavorDimensions.ENVIRONMENT to ProductFlavors.DEV) {
                unitTest { enabled = false }
            }
            onVariants.withFlavor(FlavorDimensions.ENVIRONMENT to ProductFlavors.PROD) {
                unitTest { enabled = false }
            }
        }
    }
}

private const val SLIM_TESTS_PROPERTY = "slimTests"

package io.github.reactivecircus.streamlined

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

/**
 * Apply default kapt configs to the [Project].
 */
internal fun Project.configureKapt() {
    extensions.configure<KaptExtension> {
        javacOptions {
            if (hasHiltCompilerDependency) {
                option("-Adagger.fastInit=enabled")
                option("-Adagger.strictMultibindingValidation=enabled")
                option("-Adagger.experimentalDaggerErrorMessages=enabled")
                if (isCiBuild) {
                    option("-Xmaxerrs", 500)
                } else {
                    option("-Adagger.moduleBindingValidation=ERROR")
                }
            }
        }
    }
    // disable kapt tasks for unit tests
    tasks.matching {
        it.name.startsWith("kapt") && it.name.endsWith("UnitTestKotlin")
    }.configureEach {
        enabled = false
    }
}

private val Project.hasHiltCompilerDependency: Boolean
    get() = configurations.any {
        it.dependencies.any { dependency ->
            dependency.name == "hilt-compiler"
        }
    }

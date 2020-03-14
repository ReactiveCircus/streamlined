package io.github.reactivecircus.streamlined

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

/**
 * Apply default kapt configs to the [Project].
 */
internal fun Project.configureKapt() {
    pluginManager.withPlugin("org.jetbrains.kotlin.kapt") {
        extensions.configure(KaptExtension::class.java) {
            javacOptions {
                option("-Adagger.fastInit=enabled")
                option("-Adagger.experimentalDaggerErrorMessages=enabled")
                if (isCiBuild) {
                    option("-Xmaxerrs", 500)
                } else {
                    option("-Adagger.moduleBindingValidation=ERROR")
                }
            }
        }
    }
}

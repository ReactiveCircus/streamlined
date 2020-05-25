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

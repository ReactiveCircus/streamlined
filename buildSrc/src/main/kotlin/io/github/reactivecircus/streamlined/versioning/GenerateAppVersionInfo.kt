package io.github.reactivecircus.streamlined.versioning

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * Generates both the versionName and versionCode for the app.
 */
@CacheableTask
abstract class GenerateAppVersionInfo : DefaultTask() {

    @get:Input
    abstract val majorVersion: Property<Int>

    @get:Input
    abstract val minorVersion: Property<Int>

    @get:Input
    abstract val patchVersion: Property<Int>

    @get:Optional
    @get:Input
    abstract val buildNumber: Property<Int>

    @get:OutputFile
    abstract val versionNameFile: RegularFileProperty

    @get:OutputFile
    abstract val versionCodeFile: RegularFileProperty

    @TaskAction
    fun generate() {
        val major = majorVersion.get()
        val minor = minorVersion.get()
        val patch = patchVersion.get()
        val buildNumber = buildNumber.orNull

        val versionName = "${major}.${minor}.${patch}".let {
            if (buildNumber != null) "$it.${buildNumber}" else it
        }
        versionNameFile.get().asFile.writeText(versionName)
        logger.lifecycle("Generated app version name: $versionName.")

        val versionCode = major * MAX_VERSION * MAX_VERSION + minor * MAX_VERSION + patch
        versionCodeFile.get().asFile.writeText(versionCode.toString())
        logger.lifecycle("Generated app version code: $versionCode.")
    }

    /**
     * Returns the generated app version name as a `Provider<String>`.
     */
    @Suppress("UnstableApiUsage")
    fun versionName(): Provider<String> = versionNameFile.asFile.map { file ->
        file.readText().trim()
    }

    /**
     * Returns the generated app version code as a `Provider<Int>`.
     */
    @Suppress("UnstableApiUsage")
    fun versionCode(): Provider<Int> = versionCodeFile.asFile.map { file ->
        file.readText().trim().toInt()
    }

    companion object {
        const val TASK_NAME = "generateAppVersionInfo"
        const val TASK_DESCRIPTION = "Generates both the versionName and versionCode for the app."
    }
}

package io.github.reactivecircus.streamlined.versioning

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import io.github.reactivecircus.streamlined.hasAndroidAppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.getByType
import java.io.File

/**
 * A plugin that generates and sets the version name and version code for an application by:
 * - reading the major, minor and patch versions from the configuration
 * - generating the versionName of the app in the format of "<major>.<minor>.<patch>" or "<major>.<minor>.<patch>.<buildNumber>" if `buildNumber` has been provided
 * - computing the versionCode of the app: major * MAX_VERSION ^ 2 + minor * MAX_VERSION + patch
 * - applying the computed versionName and versionCode to each application variant
 *
 * Apply this plugin to the build.gradle.kts file in an Android Application project:
 * ```
 * plugins {
 *     `app-versioning`
 * }
 *
 * appVersioning {
 *     major = 1
 *     minor = 2
 *     patch = 3
 *     buildNumber = System.getenv("BUILD_NUMBER")?.toIntOrNull() // optional
 * }
 * ```
 */
@Suppress("UnstableApiUsage")
class AppVersioningPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("appVersioning", AppVersioningExtension::class.java)
        project.afterEvaluate {
            val isAndroidAppProject = project.hasAndroidAppPlugin
            val majorVersion = extension.major
            val minorVersion = extension.minor
            val patchVersion = extension.patch
            require(isAndroidAppProject) {
                "The App versioning plugin should only be applied to an Android Application project but ${project.displayName} doesn't have the 'com.android.application' plugin applied."
            }
            requireNotNull(majorVersion) {
                "Missing major version from App versioning plugin configuration. A sample configuration:\n\n$SAMPLE_CONFIGURATION"
            }
            requireNotNull(minorVersion) {
                "Missing minor version from App versioning plugin configuration. A sample configuration:\n\n$SAMPLE_CONFIGURATION"
            }
            requireNotNull(patchVersion) {
                "Missing patch version from App versioning plugin configuration. A sample configuration:\n\n$SAMPLE_CONFIGURATION"
            }

            val generateAppVersionInfo = registerGenerateAppVersionInfoTask(
                project = this,
                extension = extension
            )

            val generatedVersionName = generateAppVersionInfo.flatMap { it.versionName() }
            val generatedVersionCode = generateAppVersionInfo.flatMap { it.versionCode() }

            tasks.register(PrintAppVersionName.TASK_NAME, PrintAppVersionName::class.java) {
                group = APP_VERSIONING_TASK_GROUP
                description = PrintAppVersionName.TASK_DESCRIPTION
                versionName.set(generatedVersionName)
            }

            tasks.register(PrintAppVersionCode.TASK_NAME, PrintAppVersionCode::class.java) {
                group = APP_VERSIONING_TASK_GROUP
                description = PrintAppVersionCode.TASK_DESCRIPTION
                versionCode.set(generatedVersionCode)
            }

            extensions.getByType<BaseAppModuleExtension>().onVariantProperties {
                outputs.forEach {
                    it.versionName.set(generatedVersionName)
                    it.versionCode.set(generatedVersionCode)
                }
            }
        }
    }

    private fun registerGenerateAppVersionInfoTask(project: Project, extension: AppVersioningExtension): TaskProvider<GenerateAppVersionInfo> =
        project.tasks.register(
            GenerateAppVersionInfo.TASK_NAME,
            GenerateAppVersionInfo::class.java
        ) {
            group = APP_VERSIONING_TASK_GROUP
            description = GenerateAppVersionInfo.TASK_DESCRIPTION

            majorVersion.set(extension.major)
            minorVersion.set(extension.minor)
            patchVersion.set(extension.patch)
            buildNumber.set(extension.buildNumber)

            versionNameFile.set(project.layout.buildDirectory.file("$APP_VERSIONING_TASK_OUTPUT_DIR/$VERSION_NAME_RESULT_FILE"))
            versionCodeFile.set(project.layout.buildDirectory.file("$APP_VERSIONING_TASK_OUTPUT_DIR/$VERSION_CODE_RESULT_FILE"))
        }
}

private val SAMPLE_CONFIGURATION = """
    appVersioning {
        major = 1
        minor = 0
        patch = 0
    }
""".trimIndent()

internal const val MAX_VERSION = 1000
internal const val APP_VERSIONING_TASK_GROUP = "App versioning"
internal const val APP_VERSIONING_TASK_OUTPUT_DIR = "outputs/app_versioning"
internal const val VERSION_NAME_RESULT_FILE = "version_name.txt"
internal const val VERSION_CODE_RESULT_FILE = "version_code.txt"

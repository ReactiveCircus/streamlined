package io.github.reactivecircus.streamlined.versioning

import android.databinding.tool.ext.capitalizeUS
import com.android.build.api.variant.VariantOutputConfiguration
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import io.github.reactivecircus.streamlined.hasAndroidAppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.language.nativeplatform.internal.BuildType

/**
 * A plugin that generates and sets the version name and version code for an Android app using the latest git tag that follows semantic versioning.
 * The latest git tag with the format MAJOR.MINOR.PATCH (without additional label for pre-release or build metadata) is used for computing version name and version code:
 * Version name is "<major>.<minor>.<patch>" or "<major>.<minor>.<patch>.<commitsSinceLatestTag>" if `commitsSinceLatestTag` is greater than 0;
 * Version code is major * 10 ^ (maxDigits * 2) + minor * 10 ^ maxDigits + patch + commitsSinceLatestTag, where `maxDigits` is the maximum number of digits allowed for a version.
 */
@Suppress("UnstableApiUsage")
class AppVersioningPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val appVersioningExtension = project.extensions.create("appVersioning", AppVersioningExtension::class.java)

        project.plugins.withType<AppPlugin> {
            project.extensions.getByType<BaseAppModuleExtension>().onVariantProperties {
                if (!appVersioningExtension.releaseBuildOnly.get() || buildType == BuildType.RELEASE.name) {
                    val generateAppVersionInfo = project.registerGenerateAppVersionInfoTask(
                        variantName = name,
                        extension = appVersioningExtension
                    )

                    val generatedVersionName = generateAppVersionInfo.flatMap { it.versionName() }
                    val generatedVersionCode = generateAppVersionInfo.flatMap { it.versionCode() }

                    project.tasks.register("${PrintAppVersionName.TASK_NAME}For${name.capitalizeUS()}", PrintAppVersionName::class.java) {
                        group = APP_VERSIONING_TASK_GROUP
                        description = PrintAppVersionName.TASK_DESCRIPTION
                        versionName.set(generatedVersionName)
                    }

                    project.tasks.register("${PrintAppVersionCode.TASK_NAME}For${name.capitalizeUS()}", PrintAppVersionCode::class.java) {
                        group = APP_VERSIONING_TASK_GROUP
                        description = PrintAppVersionCode.TASK_DESCRIPTION
                        versionCode.set(generatedVersionCode)
                    }

                    val mainOutput = outputs.single { it.outputType == VariantOutputConfiguration.OutputType.SINGLE }
                    mainOutput.versionName.set(generatedVersionName)
                    mainOutput.versionCode.set(generatedVersionCode)
                }
            }
        }
    }

    private fun Project.registerGenerateAppVersionInfoTask(variantName: String, extension: AppVersioningExtension): TaskProvider<GenerateAppVersionInfo> =
        tasks.register(
            "${GenerateAppVersionInfo.TASK_NAME}For${variantName.capitalizeUS()}",
            GenerateAppVersionInfo::class.java
        ) {
            group = APP_VERSIONING_TASK_GROUP
            description = GenerateAppVersionInfo.TASK_DESCRIPTION

            gitRefsDirectory.set(rootProject.file(GIT_REFS_DIRECTORY))
            maxDigits.set(extension.maxDigits)
            requireValidTag.set(extension.requireValidGitTag)
            fetchTagsWhenNoneExistsLocally.set(extension.fetchTagsWhenNoneExistsLocally)

            versionNameFile.set(layout.buildDirectory.file("$APP_VERSIONING_TASK_OUTPUT_DIR/$variantName/$VERSION_NAME_RESULT_FILE"))
            versionCodeFile.set(layout.buildDirectory.file("$APP_VERSIONING_TASK_OUTPUT_DIR/$variantName/$VERSION_CODE_RESULT_FILE"))
        }
}

internal const val APP_VERSIONING_TASK_GROUP = "versioning"
internal const val APP_VERSIONING_TASK_OUTPUT_DIR = "outputs/app_versioning"
internal const val GIT_REFS_DIRECTORY = ".git/refs"
internal const val VERSION_NAME_RESULT_FILE = "version_name.txt"
internal const val VERSION_CODE_RESULT_FILE = "version_code.txt"

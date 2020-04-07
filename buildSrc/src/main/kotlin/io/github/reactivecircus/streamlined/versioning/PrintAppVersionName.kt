package io.github.reactivecircus.streamlined.versioning

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * Prints the app version name to the console.
 */
abstract class PrintAppVersionName : DefaultTask() {

    @get:Input
    abstract val versionName: Property<String>

    @TaskAction
    fun print() {
        println(versionName.get())
    }

    companion object {
        const val TASK_NAME = "printAppVersionName"
        const val TASK_DESCRIPTION = "Prints the app version name to the console."
    }
}

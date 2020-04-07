package io.github.reactivecircus.streamlined.versioning

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * Prints the app version code to the console.
 */
abstract class PrintAppVersionCode : DefaultTask() {

    @get:Input
    abstract val versionCode: Property<Int>

    @TaskAction
    fun print() {
        println(versionCode.get())
    }

    companion object {
        const val TASK_NAME = "printAppVersionCode"
        const val TASK_DESCRIPTION = "Prints the app version code to the console."
    }
}

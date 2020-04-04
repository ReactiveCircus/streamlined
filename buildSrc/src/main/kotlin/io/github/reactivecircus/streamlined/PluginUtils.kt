package io.github.reactivecircus.streamlined

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.TestedExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal val Project.isRoot get() = this == this.rootProject
internal val Project.appExtension: AppExtension get() = extensions.getByType(AppExtension::class.java)
internal val Project.libraryExtension: LibraryExtension get() = extensions.getByType(LibraryExtension::class.java)
internal val Project.testedExtension: TestedExtension get() = extensions.getByType(TestedExtension::class.java)

internal val Project.hasAndroidAppPlugin get() = plugins.hasPlugin(AppPlugin::class.java)
internal val Project.hasAndroidLibraryPlugin get() = plugins.hasPlugin(LibraryPlugin::class.java)

internal val Project.hasAndroidTestSource: Boolean
    get() {
        extensions.findByType(KotlinAndroidProjectExtension::class.java)?.sourceSets?.findByName("androidTest")?.let {
            if (it.kotlin.files.isNotEmpty()) return true
        }
        return false
    }

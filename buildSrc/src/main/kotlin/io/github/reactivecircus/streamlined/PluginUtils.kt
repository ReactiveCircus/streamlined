package io.github.reactivecircus.streamlined

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.TestedExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

internal val Project.isRoot get() = this == this.rootProject
internal val Project.appExtension: AppExtension get() = extensions.getByType()
internal val Project.libraryExtension: LibraryExtension get() = extensions.getByType()
internal val Project.testedExtension: TestedExtension get() = extensions.getByType()

internal val Project.hasAndroidAppPlugin get() = plugins.hasPlugin(AppPlugin::class)
internal val Project.hasAndroidLibraryPlugin get() = plugins.hasPlugin(LibraryPlugin::class)

internal val Project.hasUnitTestSource: Boolean
    get() {
        extensions.findByType<KotlinAndroidProjectExtension>()?.sourceSets?.findByName("test")?.let {
            if (it.kotlin.files.isNotEmpty()) return true
        }
        extensions.findByType<KotlinProjectExtension>()?.sourceSets?.findByName("test")?.let {
            if (it.kotlin.files.isNotEmpty()) return true
        }
        return false
    }

internal val Project.hasAndroidTestSource: Boolean
    get() {
        extensions.findByType<KotlinAndroidProjectExtension>()?.sourceSets?.findByName("androidTest")?.let {
            if (it.kotlin.files.isNotEmpty()) return true
        }
        return false
    }

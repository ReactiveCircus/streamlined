package io.github.reactivecircus.streamlined

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

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

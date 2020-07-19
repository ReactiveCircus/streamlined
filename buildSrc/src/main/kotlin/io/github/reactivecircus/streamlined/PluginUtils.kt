package io.github.reactivecircus.streamlined

import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

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

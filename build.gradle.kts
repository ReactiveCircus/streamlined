buildscript {
    apply(from = "buildSrc/buildDependencies.gradle")
    val build: Map<Any, Any> by extra

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        // TODO remove after next AGP release
        maven("https://storage.googleapis.com/r8-releases/raw")
    }

    dependencies {
        // TODO remove after next AGP release
        classpath("com.android.tools:r8:2.2.32")
        classpath(build.getValue("androidGradlePlugin"))
        classpath(build.getValue("appVersioningGradlePlugin"))
        classpath(build.getValue("kotlinGradlePlugin"))
        classpath(build.getValue("kotlinSerializationPlugin"))
        classpath(build.getValue("googleServicesGradlePlugin"))
        classpath(build.getValue("sqldelightGradlePlugin"))
        classpath(build.getValue("playPublisherPlugin"))
    }
}

plugins {
    `streamlined-plugin`
}

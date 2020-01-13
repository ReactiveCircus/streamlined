buildscript {
    apply(from = "buildSrc/buildDependencies.gradle")
    val build: Map<Any, Any> by extra

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath(build.getValue("androidGradlePlugin"))
        classpath(build.getValue("kotlinGradlePlugin"))
        classpath(build.getValue("kotlinSerializationPlugin"))
        classpath(build.getValue("googleServicesGradlePlugin"))
        classpath(build.getValue("dexcountGradlePlugin"))
        classpath(build.getValue("sqlDelightGradlePlugin"))
    }
}

plugins {
    `streamlined-plugin`
}

apply(from = "buildDependencies.gradle")
val versions: Map<Any, Any> by extra
val build: Map<Any, Any> by extra

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    gradlePluginPortal()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

dependencies {
    implementation(build.getValue("kotlinGradlePlugin"))
    implementation(build.getValue("detektGradlePlugin"))
    implementation(build.getValue("androidGradlePlugin"))
    implementation("com.android.tools.build:builder:${versions.getValue("agp")}")
    implementation("com.android.tools.build:builder-model:${versions.getValue("agp")}")
}

gradlePlugin {
    plugins {
        register("streamlined") {
            id = "streamlined-plugin"
            implementationClass = "io.github.reactivecircus.streamlined.StreamlinedPlugin"
        }
        register("coreLibraryDesugaring") {
            id = "core-library-desugaring"
            implementationClass = "io.github.reactivecircus.streamlined.CoreLibraryDesugaringPlugin"
        }
    }
}

apply(from = "buildDependencies.gradle")
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
    implementation(build.getValue("androidGradlePlugin"))
    implementation(build.getValue("detektGradlePlugin"))
}

gradlePlugin {
    plugins {
        register("streamlined") {
            id = "streamlined-plugin"
            implementationClass = "io.github.reactivecircus.streamlined.StreamlinedPlugin"
        }
    }
}

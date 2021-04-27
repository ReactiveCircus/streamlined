import io.github.reactivecircus.streamlined.Plugins

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    // TODO Remove once updated to AGP that bundles at least r8 3.0.37-dev
    maven("https://storage.googleapis.com/r8-releases/raw")
}

dependencies {
    implementation(Plugins.kotlinGradlePlugin)
    implementation(Plugins.detektGradlePlugin)
    implementation(Plugins.dependencyGraphGeneratorPlugin)
    // TODO Remove once updated to AGP that bundles at least r8 3.0.37-dev
    implementation("com.android.tools:r8:3.0.37-dev")
    implementation(Plugins.androidGradlePlugin)
    implementation(Plugins.appVersioningGradlePlugin)
    implementation(Plugins.kotlinSerializationPlugin)
    implementation(Plugins.hiltGradlePlugin)
    implementation(Plugins.googleServicesGradlePlugin)
    implementation(Plugins.sqldelightGradlePlugin)
    implementation(Plugins.playPublisherPlugin)
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

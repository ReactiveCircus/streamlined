import io.github.reactivecircus.streamlined.Plugins

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

dependencies {
    implementation(Plugins.kotlinGradlePlugin)
    implementation(Plugins.detektGradlePlugin)
    implementation(Plugins.dependencyGraphGeneratorPlugin)
    implementation(Plugins.androidGradlePlugin)
    implementation(Plugins.appVersioningGradlePlugin)
    implementation(Plugins.kotlinSerializationPlugin)
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

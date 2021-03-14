import io.github.reactivecircus.streamlined.Plugins

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven("https://storage.googleapis.com/r8-releases/raw")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

dependencies {
    implementation(Plugins.kotlinGradlePlugin)
    implementation(Plugins.detektGradlePlugin)
    implementation(Plugins.dependencyGraphGeneratorPlugin)
    // TODO Remove once https://issuetracker.google.com/issues/181493713 is fixed
    @Suppress("UnstableApiUsage")
    if (providers.gradleProperty("forceStableR8").forUseAtConfigurationTime().isPresent) {
        implementation("com.android.tools:r8:2.2.60")
    }
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

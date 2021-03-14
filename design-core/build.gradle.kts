import io.github.reactivecircus.streamlined.Libraries
import io.github.reactivecircus.streamlined.Versions

plugins {
    `streamlined-plugin`
    id("com.android.library")
    kotlin("android")
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.androidx.compose
}

dependencies {
    api(project(":design-theme"))
    api(project(":design-foundation"))

    // Compose
    implementation(Libraries.androidx.compose.ui)
    implementation(Libraries.androidx.compose.tooling)
    implementation(Libraries.androidx.compose.foundation)
    implementation(Libraries.androidx.compose.material)
    implementation(Libraries.androidx.compose.materialIconsExtended)
}

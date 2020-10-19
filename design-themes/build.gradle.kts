import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    kotlin("android")
}

android.buildFeatures.androidResources = true

dependencies {
    // AndroidX
    implementation(libraries.androidx.appCompat)

    // Material Components
    implementation(libraries.material)
}

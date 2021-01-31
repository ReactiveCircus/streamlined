import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    kotlin("android")
}

android.buildFeatures.androidResources = true

dependencies {
    // AndroidX
    implementation(Libraries.androidx.appCompat)

    // Material Components
    implementation(Libraries.material)
}

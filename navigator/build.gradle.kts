import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
}

android.buildFeatures.androidResources = true

dependencies {
    // AndroidX
    implementation(libraries.androidx.annotation)
    implementation(libraries.androidx.fragment.ktx)
    implementation(libraries.androidx.navigation.fragmentKtx)

    // timber
    implementation(libraries.timber)
}

import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
}

android {
    namespace = "io.github.reactivecircus.streamlined.navigator"
    buildFeatures.androidResources = true
}

dependencies {
    // AndroidX
    implementation(Libraries.androidx.annotation)
    implementation(Libraries.androidx.fragment.ktx)
    implementation(Libraries.androidx.navigation.fragmentKtx)

    // timber
    implementation(Libraries.timber)
}

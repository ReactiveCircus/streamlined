import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("kotlin-android")
}

dependencies {
    // AndroidX
    implementation(libraries.androidx.appCompat)

    // Material Components
    implementation(libraries.material)
}

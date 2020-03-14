import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("kotlin-android")
}

dependencies {
    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)

    // AndroidX
    implementation(libraries.androidx.annotation)
    implementation(libraries.androidx.lifecycle.liveDataKtx)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.truth)
    testImplementation(libraries.androidx.archCore.testing)
}

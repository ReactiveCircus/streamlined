import io.github.reactivecircus.streamlined.build.libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("kotlin-android")
}

dependencies {
    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)
}

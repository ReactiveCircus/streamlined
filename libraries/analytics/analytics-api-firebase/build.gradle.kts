import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("kotlin-android")
}

dependencies {
    api(project(":analytics-api-base"))

    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)

    // Firebase
    api(libraries.firebase.analyticsKtx)
}

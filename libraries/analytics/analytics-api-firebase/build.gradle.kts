import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    kotlin("android")
}

dependencies {
    api(project(":analytics-api-base"))

    // Firebase
    api(libraries.firebase.analyticsKtx)
}

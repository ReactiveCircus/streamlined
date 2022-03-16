import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "io.github.reactivecircus.analytics.firebase"
}

dependencies {
    api(project(":analytics-api-base"))

    // Firebase
    api(Libraries.firebase.analyticsKtx)
}

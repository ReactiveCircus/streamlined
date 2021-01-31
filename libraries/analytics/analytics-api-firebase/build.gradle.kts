import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    kotlin("android")
}

dependencies {
    api(project(":analytics-api-base"))

    // Firebase
    api(Libraries.firebase.analyticsKtx)
}

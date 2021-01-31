import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    kotlin("android")
}

dependencies {
    // timber
    implementation(Libraries.timber)

    // Bugsnag
    api(Libraries.bugsnag) {
        exclude(module = "bugsnag-plugin-android-ndk")
    }
}

import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("kotlin-android")
}

dependencies {
    // timber
    implementation(libraries.timber)

    // Bugsnag
    api(libraries.bugsnag) {
        exclude(module = "bugsnag-plugin-android-ndk")
    }
}

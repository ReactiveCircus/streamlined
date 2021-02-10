import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    `android-library`
    `kotlin-android`
    `kotlin-kapt`
}

dependencies {
    implementation(project(":domain-runtime"))

    // Coroutines
    implementation(Libraries.kotlinx.coroutines.core)

    // AndroidX
    implementation(Libraries.androidx.work.runtimeKtx)

    // Hilt
    implementation(Libraries.hilt.android)
    kapt(Libraries.hilt.compiler)

    // Hilt AndroidX
    implementation(Libraries.androidx.hilt.work)
    kapt(Libraries.androidx.hilt.compiler)

    // timber
    implementation(Libraries.timber)
}

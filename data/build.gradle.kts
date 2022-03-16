import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    `core-library-desugaring`
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "io.github.reactivecircus.streamlined.data"
}

dependencies {
    implementation(project(":domain-api"))
    implementation(project(":remote-base"))
    api(project(":persistence"))
    api(project(":store-ext"))

    // Blueprint
    implementation(Libraries.blueprint.asyncCoroutines)

    // Hilt
    implementation(Libraries.hilt.android)
    kapt(Libraries.hilt.compiler)

    // timber
    implementation(Libraries.timber)

    // Unit tests
    testImplementation(Libraries.junit)
    testImplementation(Libraries.truth)
    testImplementation(project(":coroutines-test-ext"))
}

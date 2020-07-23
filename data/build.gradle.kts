import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    `core-library-desugaring`
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(":domain-api"))
    implementation(project(":remote-base"))
    api(project(":persistence"))
    api(project(":store-ext"))

    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)

    // Blueprint
    implementation(libraries.blueprint.asyncCoroutines)

    // Dagger
    implementation(libraries.dagger.runtime)
    kapt(libraries.dagger.compiler)

    // timber
    implementation(libraries.timber)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.truth)
    testImplementation(project(":coroutines-test-ext"))
}

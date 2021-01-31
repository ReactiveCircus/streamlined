import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    `core-library-desugaring`
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

dependencies {
    implementation(project(":domain-api"))
    implementation(project(":remote-base"))
    api(project(":persistence"))
    api(project(":store-ext"))

    // Blueprint
    implementation(Libraries.blueprint.asyncCoroutines)

    // Dagger
    implementation(Libraries.dagger.runtime)
    kapt(Libraries.dagger.compiler)

    // timber
    implementation(Libraries.timber)

    // Unit tests
    testImplementation(Libraries.junit)
    testImplementation(Libraries.truth)
    testImplementation(project(":coroutines-test-ext"))
}

import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    kotlin("jvm")
    kotlin("kapt")
    id("com.android.lint")
}

dependencies {
    api(project(":domain-api"))

    // Blueprint
    api(libraries.blueprint.interactorCoroutines)
    api(libraries.blueprint.asyncCoroutines)

    // Dagger
    implementation(libraries.dagger.runtime)
    kapt(libraries.dagger.compiler)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.truth)
    testImplementation(project(":domain-testing"))
    testImplementation(project(":coroutines-test-ext"))
}

import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("kotlin")
}

dependencies {
    implementation(project(":domain-api"))

    // Coroutines
    implementation(libraries.kotlinx.coroutines.core)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.truth)
    testImplementation(project(":coroutines-test-ext"))
}

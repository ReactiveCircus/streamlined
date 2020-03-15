import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("kotlin")
}

dependencies {
    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)

    // Coroutines
    implementation(libraries.kotlinx.coroutines.core)

    // Store
    implementation(libraries.store)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.truth)
    testImplementation(project(":coroutines-test-ext"))
}

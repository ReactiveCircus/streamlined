import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    kotlin("jvm")
    id("com.android.lint")
}

dependencies {
    implementation(project(":domain-api"))

    // Coroutines
    implementation(Libraries.kotlinx.coroutines.core)

    // Unit tests
    testImplementation(Libraries.junit)
    testImplementation(Libraries.truth)
    testImplementation(project(":coroutines-test-ext"))
}

import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    kotlin("jvm")
    id("com.android.lint")
}

dependencies {
    // Coroutines
    implementation(Libraries.kotlinx.coroutines.core)

    // Store
    implementation(Libraries.store)

    // Unit tests
    testImplementation(Libraries.junit)
    testImplementation(Libraries.truth)
    testImplementation(project(":coroutines-test-ext"))
}

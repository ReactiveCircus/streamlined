import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    kotlin("jvm")
    id("com.android.lint")
}

dependencies {
    // Coroutines
    api(libraries.kotlinx.coroutines.core)

    // Store
    api(libraries.store)
}

import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("kotlin")
}

dependencies {
    // Coroutines
    api(libraries.kotlinx.coroutines.core)

    // Store
    api(libraries.store)
}

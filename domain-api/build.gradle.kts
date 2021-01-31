import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    kotlin("jvm")
    id("com.android.lint")
}

dependencies {
    // Coroutines
    api(Libraries.kotlinx.coroutines.core)

    // Store
    api(Libraries.store)
}

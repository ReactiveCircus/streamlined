import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.android.lint")
}

dependencies {
    // Retrofit
    implementation(libraries.retrofit.client)

    // Serialization
    implementation(libraries.kotlinx.serialization)
}

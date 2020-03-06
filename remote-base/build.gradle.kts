import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("kotlin")
    id("kotlinx-serialization")
}

dependencies {
    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)

    // Retrofit
    implementation(libraries.retrofit.client)

    // Serialization
    implementation(libraries.kotlinx.serialization)
}

import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("kotlin")
    id("kotlinx-serialization")
}

dependencies {
    // Retrofit
    implementation(libraries.retrofit.client)

    // Serialization
    implementation(libraries.kotlinx.serialization)
}

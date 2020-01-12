import io.github.reactivecircus.streamlined.build.libraries

plugins {
    `streamlined-plugin`
    id("kotlin")
    id("kotlinx-serialization")
}

dependencies {
    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)

    // Coroutines
    implementation(libraries.kotlinx.coroutines.core)

    // OkHttp
    implementation(libraries.okhttp.client)
    implementation(libraries.okhttp.loggingInterceptor)

    // Retrofit
    implementation(libraries.retrofit.client)
    implementation(libraries.retrofit.serializationConverter)

    // Serialization
    implementation(libraries.kotlinx.serialization)

    // Dagger
    implementation(libraries.dagger.runtime)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.mockk)
    testImplementation(libraries.truth)
    testImplementation(libraries.okhttp.mockWebServer)
}

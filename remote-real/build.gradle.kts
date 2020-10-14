import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    api(project(":remote-base"))

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
    kapt(libraries.dagger.compiler)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.truth)
    testImplementation(libraries.okhttp.mockWebServer)
}

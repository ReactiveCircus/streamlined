import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    kotlin("jvm")
    kotlin("kapt")
    id("com.android.lint")
}

dependencies {
    api(project(":remote-base"))

    // Coroutines
    implementation(Libraries.kotlinx.coroutines.core)

    // OkHttp
    implementation(Libraries.okhttp.client)
    implementation(Libraries.okhttp.loggingInterceptor)

    // Retrofit
    implementation(Libraries.retrofit.client)
    implementation(Libraries.retrofit.serializationConverter)

    // Serialization
    implementation(Libraries.kotlinx.serialization)

    // Hilt
    implementation(Libraries.hilt.core)
    kapt(Libraries.hilt.compiler)

    // Unit tests
    testImplementation(Libraries.junit)
    testImplementation(Libraries.truth)
    testImplementation(Libraries.okhttp.mockWebServer)
}

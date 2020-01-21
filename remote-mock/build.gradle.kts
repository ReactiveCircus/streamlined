import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("kotlin")
    id("kotlin-kapt")
}

dependencies {
    api(project(":remote-base"))

    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)

    // OkHttp
    implementation(libraries.okhttp.client)
    implementation(libraries.okhttp.loggingInterceptor)

    // Retrofit
    implementation(libraries.retrofit.client)
    implementation(libraries.retrofit.mock)

    // Dagger
    implementation(libraries.dagger.runtime)
    kapt(libraries.dagger.compiler)
}

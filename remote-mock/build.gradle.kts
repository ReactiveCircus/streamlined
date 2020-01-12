import io.github.reactivecircus.streamlined.build.libraries

plugins {
    `streamlined-plugin`
    id("kotlin")
}

dependencies {
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
}

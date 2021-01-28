import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    kotlin("jvm")
    kotlin("kapt")
    id("com.android.lint")
}

dependencies {
    api(project(":remote-base"))

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

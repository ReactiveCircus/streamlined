import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    kotlin("jvm")
    kotlin("kapt")
    id("com.android.lint")
}

dependencies {
    api(project(":remote-base"))

    // OkHttp
    implementation(Libraries.okhttp.client)
    implementation(Libraries.okhttp.loggingInterceptor)

    // Retrofit
    implementation(Libraries.retrofit.client)
    implementation(Libraries.retrofit.mock)

    // Hilt
    implementation(Libraries.hilt.core)
    kapt(Libraries.hilt.compiler)
}

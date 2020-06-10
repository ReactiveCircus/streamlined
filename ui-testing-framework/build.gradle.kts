import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(":analytics-api-no-op"))
    implementation(project(":navigator"))
    implementation(project(":ui-common"))
    implementation(project(":domain-api"))
    implementation(project(":data"))
    implementation(project(":remote-mock"))

    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)

    // Blueprint
    implementation(libraries.blueprint.asyncCoroutines)

    // Dagger
    implementation(libraries.dagger.runtime)
    kapt(libraries.dagger.compiler)

    // OkHttp
    implementation(libraries.okhttp.client)
    implementation(libraries.okhttp.loggingInterceptor)

    // Retrofit
    implementation(libraries.retrofit.client)
    implementation(libraries.retrofit.mock)

    // timber
    implementation(libraries.timber)

    implementation(libraries.androidx.fragment.testing)
    api(libraries.blueprint.testingRobot)
    api(libraries.androidx.test.coreKtx)
    api(libraries.androidx.test.monitor)
    api(libraries.androidx.test.runner)
    api(libraries.androidx.test.rules)
    api(libraries.androidx.test.ext.junitKtx)
    api(libraries.androidx.test.ext.truth)
    api(libraries.androidx.espresso.core)
    api(libraries.androidx.espresso.contrib)
    api(libraries.androidx.espresso.intents)
    api(libraries.truth)
}

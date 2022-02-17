import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("com.google.dagger.hilt.android")
    kotlin("android")
    kotlin("kapt")
}

dependencies {
    implementation(project(":analytics-api-no-op"))
    implementation(project(":navigator"))
    implementation(project(":ui-common"))
    implementation(project(":domain-api"))
    api(project(":data"))
    api(project(":remote-mock"))

    // Blueprint
    implementation(Libraries.blueprint.asyncCoroutines)

    // Hilt
    implementation(Libraries.hilt.android)
    api(Libraries.hilt.androidTesting)
    kapt(Libraries.hilt.compiler)

    // OkHttp
    implementation(Libraries.okhttp.client)
    implementation(Libraries.okhttp.loggingInterceptor)

    // Retrofit
    api(Libraries.retrofit.client)
    api(Libraries.retrofit.mock)

    // timber
    implementation(Libraries.timber)

    implementation(Libraries.androidx.fragment.testing)
    implementation(Libraries.radiography)
    api(Libraries.blueprint.testingRobot)
    api(Libraries.androidx.test.coreKtx)
    api(Libraries.androidx.test.monitor)
    api(Libraries.androidx.test.runner)
    api(Libraries.androidx.test.rules)
    api(Libraries.androidx.test.ext.junitKtx)
    api(Libraries.androidx.test.ext.truth)
    api(Libraries.androidx.espresso.core)
    api(Libraries.androidx.espresso.contrib)
    api(Libraries.androidx.espresso.intents)
    api(Libraries.truth)
}

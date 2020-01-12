import io.github.reactivecircus.streamlined.build.libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    defaultConfig {
        testApplicationId = "io.github.reactivecircus.streamlined.work.test"
        testInstrumentationRunner = "io.github.reactivecircus.streamlined.work.ScreenTestRunner"
    }
}

dependencies {
    implementation(project(":domain"))

    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)

    // Coroutines
    implementation(libraries.kotlinx.coroutines.core)

    // AndroidX
    implementation(libraries.androidx.work.runtimeKtx)

    // Dagger
    implementation(libraries.dagger.runtime)
    kapt(libraries.dagger.compiler)

    // timber
    implementation(libraries.timber)

    // Android tests
    androidTestImplementation(libraries.androidx.test.coreKtx)
    androidTestImplementation(libraries.androidx.test.monitor)
    androidTestImplementation(libraries.androidx.test.runner)
    androidTestImplementation(libraries.androidx.test.rules)
    androidTestImplementation(libraries.androidx.test.ext.junitKtx)
    androidTestImplementation(libraries.androidx.espresso.core)
    androidTestImplementation(libraries.androidx.work.testing)
    androidTestImplementation(libraries.truth)
}

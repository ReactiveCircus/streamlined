import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    @Suppress("UnstableApiUsage")
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        testApplicationId = "io.github.reactivecircus.streamlined.settings.test"
        testInstrumentationRunner = "io.github.reactivecircus.streamlined.testing.ScreenTestRunner"
    }
}

dependencies {
    implementation(project(":navigator"))
    implementation(project(":ui-common"))
    implementation(project(":domain-runtime"))

    // Coroutines
    implementation(libraries.kotlinx.coroutines.core)

    // AndroidX
    implementation(libraries.androidx.lifecycle.viewModelKtx)
    implementation(libraries.androidx.lifecycle.commonJava8)

    // Dagger
    implementation(libraries.dagger.runtime)
    kapt(libraries.dagger.compiler)

    // timber
    implementation(libraries.timber)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.truth)
    testImplementation(project(":coroutines-test-ext"))

    // Android tests
    androidTestImplementation(project(":ui-testing-framework"))
    debugImplementation(libraries.androidx.fragment.testing) {
        exclude(group = "androidx.test")
    }
    kaptAndroidTest(libraries.dagger.compiler)
}

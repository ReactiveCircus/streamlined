import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    buildFeatures {
        viewBinding = true
        androidResources = true
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
    implementation(Libraries.kotlinx.coroutines.core)

    // AndroidX
    implementation(Libraries.androidx.lifecycle.viewModelKtx)
    implementation(Libraries.androidx.lifecycle.commonJava8)

    // Dagger
    implementation(Libraries.dagger.runtime)
    kapt(Libraries.dagger.compiler)

    // timber
    implementation(Libraries.timber)

    // Unit tests
    testImplementation(Libraries.junit)
    testImplementation(Libraries.truth)
    testImplementation(project(":coroutines-test-ext"))

    // Android tests
    androidTestImplementation(project(":ui-testing-framework"))
    debugImplementation(Libraries.androidx.fragment.testing) {
        exclude(group = "androidx.test")
    }
    kaptAndroidTest(Libraries.dagger.compiler)
}

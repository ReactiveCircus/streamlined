import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    `core-library-desugaring`
    id("com.android.library")
    id("com.google.dagger.hilt.android")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "io.github.reactivecircus.streamlined.home"

    buildFeatures {
        viewBinding = true
        androidResources = true
    }

    defaultConfig {
        testApplicationId = "io.github.reactivecircus.streamlined.home.test"
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
    implementation(Libraries.androidx.swipeRefreshLayout)
    implementation(Libraries.androidx.recyclerView)
    implementation(Libraries.androidx.lifecycle.viewModelKtx)
    implementation(Libraries.androidx.lifecycle.commonJava8)

    // Hilt
    implementation(Libraries.hilt.android)
    kapt(Libraries.hilt.compiler)

    // timber
    implementation(Libraries.timber)

    // Unit tests
    testImplementation(Libraries.junit)
    testImplementation(Libraries.truth)
    testImplementation(Libraries.workflow.testing)
    testImplementation(project(":domain-testing"))
    testImplementation(project(":coroutines-test-ext"))

    // Android tests
    androidTestImplementation(project(":ui-testing-framework"))
    debugImplementation(Libraries.androidx.fragment.testing) {
        exclude(group = "androidx.test")
    }
    kaptAndroidTest(Libraries.hilt.compiler)
}

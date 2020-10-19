import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    `core-library-desugaring`
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
        testApplicationId = "io.github.reactivecircus.streamlined.home.test"
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
    implementation(libraries.androidx.swipeRefreshLayout)
    implementation(libraries.androidx.recyclerView)
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
    testImplementation(libraries.workflow.testing)
    testImplementation(project(":domain-testing"))
    testImplementation(project(":coroutines-test-ext"))

    // Android tests
    androidTestImplementation(project(":ui-testing-framework"))
    debugImplementation(libraries.androidx.fragment.testing) {
        exclude(group = "androidx.test")
    }
    kaptAndroidTest(libraries.dagger.compiler)
}

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
        testApplicationId = "io.github.reactivecircus.streamlined.storydetails.test"
        testInstrumentationRunner = "io.github.reactivecircus.streamlined.storydetails.ScreenTestRunner"
    }
}

dependencies {
    implementation(project(":router"))
    implementation(project(":ui-common"))
    implementation(project(":domain"))

    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)

    // Coroutines
    implementation(libraries.kotlinx.coroutines.core)

    // AndroidX
    implementation(libraries.androidx.swipeRefreshLayout)
    implementation(libraries.androidx.archCore.runtime)
    implementation(libraries.androidx.lifecycle.viewModelKtx)
    implementation(libraries.androidx.lifecycle.liveDataKtx)
    implementation(libraries.androidx.lifecycle.commonJava8)

    // Dagger
    implementation(libraries.dagger.runtime)
    kapt(libraries.dagger.compiler)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.mockk)
    testImplementation(libraries.truth)
    testImplementation(libraries.androidx.archCore.testing)
    testImplementation(libraries.kotlinx.coroutines.test)
    testImplementation(project(":coroutines-testing"))

    // Android tests
    androidTestImplementation(project(":ui-testing-framework"))
    debugImplementation(libraries.androidx.fragment.testing) {
        exclude(group = "androidx.test")
    }
}

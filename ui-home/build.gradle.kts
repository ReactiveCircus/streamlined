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
    compileOptions.coreLibraryDesugaringEnabled = true

    defaultConfig {
        testApplicationId = "io.github.reactivecircus.streamlined.home.test"
        testInstrumentationRunner = "io.github.reactivecircus.streamlined.testing.ScreenTestRunner"
    }
}

dependencies {
    implementation(project(":navigator"))
    implementation(project(":ui-common"))
    implementation(project(":domain"))

    // Java 8 desugaring
    coreLibraryDesugaring(libraries.desugarLibs)

    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)

    // Coroutines
    implementation(libraries.kotlinx.coroutines.core)

    // FlowBinding
    implementation(libraries.flowbinding.swipeRefreshLayout)

    // AndroidX
    implementation(libraries.androidx.swipeRefreshLayout)
    implementation(libraries.androidx.recyclerView)
    implementation(libraries.androidx.archCore.runtime)
    implementation(libraries.androidx.lifecycle.viewModelKtx)
    implementation(libraries.androidx.lifecycle.liveDataKtx)
    implementation(libraries.androidx.lifecycle.commonJava8)

    // Dagger
    implementation(libraries.dagger.runtime)
    kapt(libraries.dagger.compiler)

    // timber
    implementation(libraries.timber)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.mockk)
    testImplementation(libraries.truth)
    testImplementation(libraries.androidx.archCore.testing)
    testImplementation(project(":coroutines-test-ext"))

    // Android tests
    androidTestImplementation(project(":ui-testing-framework"))
    debugImplementation(libraries.androidx.fragment.testing) {
        exclude(group = "androidx.test")
    }
    kaptAndroidTest(libraries.dagger.compiler)
}

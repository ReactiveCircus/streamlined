import io.github.reactivecircus.streamlined.Libraries
import io.github.reactivecircus.streamlined.Versions

plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

hilt.enableExperimentalClasspathAggregation = true

android {
    buildFeatures {
        androidResources = true
        compose = true
    }

    composeOptions.kotlinCompilerExtensionVersion = Versions.androidx.compose

    defaultConfig {
        testApplicationId = "io.github.reactivecircus.streamlined.ui.headlines.test"
        testInstrumentationRunner = "io.github.reactivecircus.streamlined.ui.testing.ScreenTestRunner"
    }
}

dependencies {
    implementation(project(":navigator"))
    implementation(project(":ui-common"))
    implementation(project(":design-core"))
    implementation(project(":domain-runtime"))

    // Coroutines
    implementation(Libraries.kotlinx.coroutines.core)

    // AndroidX
    implementation(Libraries.androidx.lifecycle.viewModelKtx)
    implementation(Libraries.androidx.lifecycle.commonJava8)

    // Compose
    implementation(Libraries.androidx.compose.ui)
    implementation(Libraries.androidx.compose.tooling)
    implementation(Libraries.androidx.compose.foundation)
    implementation(Libraries.androidx.compose.material)

    // insets
    implementation(Libraries.accompanist.insets)

    // Hilt
    implementation(Libraries.hilt.android)
    kapt(Libraries.hilt.compiler)

    // timber
    implementation(Libraries.timber)

    // Unit tests
    testImplementation(Libraries.junit)
    testImplementation(Libraries.truth)
    testImplementation(project(":coroutines-test-ext"))

    // Android tests
    androidTestImplementation(Libraries.androidx.compose.test)
    androidTestImplementation(project(":ui-testing-framework"))
    debugImplementation(Libraries.androidx.fragment.testing) {
        exclude(group = "androidx.test")
    }
    kaptAndroidTest(Libraries.hilt.compiler)
}

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
        testApplicationId = "io.github.reactivecircus.streamlined.settings.test"
        testInstrumentationRunner = "io.github.reactivecircus.streamlined.testing.ScreenTestRunner"
    }
}

dependencies {
    implementation(project(":navigator"))
    implementation(project(":ui-common"))
    implementation(project(":design-core"))
    implementation(project(":domain-runtime"))

    implementation("androidx.activity:activity-compose:1.3.0-alpha03")

    // Coroutines
    implementation(Libraries.kotlinx.coroutines.core)

    // AndroidX
    implementation(Libraries.androidx.lifecycle.viewModelKtx)
    implementation(Libraries.androidx.lifecycle.commonJava8)

    // Compose
    implementation(Libraries.androidx.compose.tooling)
    implementation(Libraries.androidx.compose.layout)
    implementation(Libraries.androidx.compose.material)

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

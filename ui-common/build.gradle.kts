import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    `core-library-desugaring`
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android.buildFeatures.androidResources = true

dependencies {
    api(project(":analytics-api-base"))
    api(project(":design-themes"))

    // Blueprint
    api(Libraries.blueprint.ui)

    // Workflow
    api(Libraries.workflow.ui)

    // AndroidX
    api(Libraries.androidx.annotation)
    api(Libraries.androidx.appCompat)
    api(Libraries.androidx.constraintLayout)
    api(Libraries.androidx.coordinatorLayout)
    api(Libraries.androidx.activity.ktx)
    api(Libraries.androidx.fragment.ktx)
    api(Libraries.androidx.core.ktx)
    api(Libraries.androidx.navigation.fragmentKtx)
    api(Libraries.androidx.navigation.uiKtx)
    implementation(Libraries.androidx.lifecycle.runtimeKtx)

    // Material Components
    api(Libraries.material)

    // Window inset handling
    api(Libraries.insetter)

    // Image loading
    api(Libraries.coil)

    // Hilt
    implementation(Libraries.hilt.android)
    kapt(Libraries.hilt.compiler)

    // Unit tests
    testImplementation(Libraries.junit)
    testImplementation(Libraries.truth)
}

import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

dependencies {
    api(project(":analytics-api-base"))
    api(project(":design-themes"))

    // Blueprint
    api(libraries.blueprint.ui)

    // FlowBinding
    api(libraries.flowbinding.android)
    api(libraries.flowbinding.material)

    // FlowRedux
    api(libraries.flowRedux.dsl)

    // AndroidX
    api(libraries.androidx.annotation)
    api(libraries.androidx.appCompat)
    api(libraries.androidx.constraintLayout)
    api(libraries.androidx.coordinatorLayout)
    api(libraries.androidx.activity.ktx)
    api(libraries.androidx.fragment.ktx)
    api(libraries.androidx.core.ktx)
    api(libraries.androidx.navigation.fragmentKtx)
    api(libraries.androidx.navigation.uiKtx)

    // Material Components
    api(libraries.material)

    // Window inset handling
    api(libraries.insetter)

    // Image loading
    api(libraries.coil)

    // Dagger
    implementation(libraries.dagger.runtime)
    kapt(libraries.dagger.compiler)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.truth)
}

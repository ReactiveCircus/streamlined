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
        // enabled new Java 8 language APIs
        compileOptions.coreLibraryDesugaringEnabled = true
    }
}

dependencies {
    api(project(":analytics-api-base"))

    // Java 8 desugaring
    coreLibraryDesugaring(libraries.desugarLibs)

    // Blueprint
    api(libraries.blueprint.ui)

    // FlowBinding
    api(libraries.flowbinding.android)
    api(libraries.flowbinding.material)

    // FlowRedux
    // api(libraries.flowRedux.dsl)

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

    // Image loading
    api(libraries.coil)

    // Dagger
    implementation(libraries.dagger.runtime)
    kapt(libraries.dagger.compiler)

    // Unit tests
    testImplementation(libraries.junit)
    testImplementation(libraries.truth)
}

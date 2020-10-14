import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    `android-library`
    `kotlin-android`
    `kotlin-kapt`
}

dependencies {
    implementation(project(":domain-runtime"))

    // Coroutines
    implementation(libraries.kotlinx.coroutines.core)

    // AndroidX
    implementation(libraries.androidx.work.runtimeKtx)

    // Dagger
    implementation(libraries.dagger.runtime)
    kapt(libraries.dagger.compiler)

    // timber
    implementation(libraries.timber)
}

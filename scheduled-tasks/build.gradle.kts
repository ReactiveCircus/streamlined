import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    `android-library`
    `kotlin-android`
    `kotlin-kapt`
}

dependencies {
    implementation(project(":domain-runtime"))

    // Coroutines
    implementation(Libraries.kotlinx.coroutines.core)

    // AndroidX
    implementation(Libraries.androidx.work.runtimeKtx)

    // Dagger
    implementation(Libraries.dagger.runtime)
    kapt(Libraries.dagger.compiler)

    // timber
    implementation(Libraries.timber)
}

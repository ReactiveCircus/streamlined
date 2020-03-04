import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("kotlin-android")
}

dependencies {
    implementation(libraries.kotlinStdlib)
    implementation(libraries.androidx.lifecycle.liveDataKtx)
}

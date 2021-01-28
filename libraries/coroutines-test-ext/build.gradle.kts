import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    kotlin("jvm")
    id("com.android.lint")
}

dependencies {
    implementation(libraries.junit)
    implementation(libraries.truth)
    implementation(libraries.kotlinx.coroutines.core)
    api(libraries.kotlinx.coroutines.test)
}

import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("kotlin")
}

dependencies {
    implementation(libraries.junit)
    implementation(libraries.truth)
    implementation(libraries.kotlinx.coroutines.core)
    api(libraries.kotlinx.coroutines.test)
}

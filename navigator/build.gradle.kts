import io.github.reactivecircus.streamlined.libraries

plugins {
    `streamlined-plugin`
    id("kotlin")
}

dependencies {
    // Kotlin stdlib
    implementation(libraries.kotlinStdlib)
}

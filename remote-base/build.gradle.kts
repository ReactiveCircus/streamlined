import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.android.lint")
}

dependencies {
    // Retrofit
    implementation(Libraries.retrofit.client)

    // Serialization
    implementation(Libraries.kotlinx.serialization)
}

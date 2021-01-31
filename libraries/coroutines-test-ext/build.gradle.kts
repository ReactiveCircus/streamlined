import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    kotlin("jvm")
    id("com.android.lint")
}

dependencies {
    implementation(Libraries.junit)
    implementation(Libraries.truth)
    implementation(Libraries.kotlinx.coroutines.core)
    api(Libraries.kotlinx.coroutines.test)
}

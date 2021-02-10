import io.github.reactivecircus.streamlined.Libraries

plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("com.squareup.sqldelight")
    kotlin("android")
    kotlin("kapt")
}

sqldelight {
    database("StreamlinedDatabase") {
        schemaOutputDirectory = file("src/main/sqldelight/databases")
    }
}

dependencies {
    // SQLDelight
    implementation(Libraries.sqldelight.driver.android)
    implementation(Libraries.sqldelight.coroutinesExtensions)

    // Coroutines
    implementation(Libraries.kotlinx.coroutines.core)

    // Hilt
    implementation(Libraries.hilt.android)
    kapt(Libraries.hilt.compiler)

    // timber
    implementation(Libraries.timber)

    // Unit tests
    testImplementation(Libraries.junit)
    testImplementation(Libraries.truth)
    testImplementation(Libraries.sqldelight.driver.jvm)
    testImplementation(project(":coroutines-test-ext"))
}

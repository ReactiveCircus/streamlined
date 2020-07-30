plugins {
    `streamlined-plugin`
    id("com.android.library")
    id("kotlin-android")
}

dependencies {
    api(project(":analytics-api-base"))
}

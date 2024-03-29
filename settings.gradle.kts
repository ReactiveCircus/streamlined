rootProject.name= "streamlined"

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

include(":app")
include(":navigator")
include(":design-themes")
include(":ui-common")
include(":ui-home")
include(":ui-headlines")
include(":ui-reading-list")
include(":ui-settings")
include(":ui-story-details")
include(":ui-testing-framework")
include(":domain-api")
include(":domain-runtime")
include(":domain-testing")
include(":remote-base")
include(":remote-mock")
include(":remote-real")
include(":persistence")
include(":data")
include(":scheduled-tasks")
includeProject(":store-ext", "libraries/store-ext")
includeProject(":bugsnag-tree", "libraries/bugsnag-tree")
includeProject(":analytics-api-base", "libraries/analytics/analytics-api-base")
includeProject(":analytics-api-firebase", "libraries/analytics/analytics-api-firebase")
includeProject(":analytics-api-no-op", "libraries/analytics/analytics-api-no-op")
includeProject(":coroutines-test-ext", "libraries/coroutines-test-ext")

fun includeProject(name: String, filePath: String) {
    include(name)
    project(name).projectDir = File(filePath)
}

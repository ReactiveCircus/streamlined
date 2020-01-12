rootProject.name= "streamlined"

include(":app")
include(":router")
include(":ui-common")
include(":ui-home")
include(":ui-headlines")
include(":ui-reading-list")
include(":ui-settings")
include(":ui-story-details")
include(":ui-testing-framework")
include(":domain")
include(":remote-base")
include(":remote-mock")
include(":remote-real")
include(":persistence")
include(":data")
include(":periodic-work")
includeProject(":bugsnag-tree", "libraries/bugsnag-tree")
includeProject(":analytics-api-base", "libraries/analytics/analytics-api-base")
includeProject(":analytics-api-firebase", "libraries/analytics/analytics-api-firebase")
includeProject(":analytics-api-no-op", "libraries/analytics/analytics-api-no-op")
includeProject(":coroutines-testing", "libraries/coroutines-testing")

fun includeProject(name: String, filePath: String) {
    include(name)
    project(name).projectDir = File(filePath)
}

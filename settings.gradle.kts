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
includeProject(":bugsnag-tree", "library/bugsnag-tree")
includeProject(":analytics-api-base", "library/analytics/analytics-api-base")
includeProject(":analytics-api-firebase", "library/analytics/analytics-api-firebase")
includeProject(":analytics-api-no-op", "library/analytics/analytics-api-no-op")
includeProject(":coroutines-testing", "library/coroutines-testing")

fun includeProject(name: String, filePath: String) {
    include(name)
    project(name).projectDir = File(filePath)
}

package io.github.reactivecircus.streamlined.versioning

/**
 * Extension for [AppVersioningPlugin].
 */
open class AppVersioningExtension {

    /**
     * Major version of the app. Must be greater than 0.
     */
    var major: Int? = null
        set(value) {
            require(value!! > 0) {
                "Major version must be greater than 0."
            }
            field = value
        }

    /**
     * Minor version of the app.
     * Must be greater than or equal to 0 and less than [MAX_VERSION].
     */
    var minor: Int? = null
        set(value) {
            require(value in 0 until MAX_VERSION) {
                "Minor version must be greater than or equal to 0 and less than $MAX_VERSION."
            }
            field = value
        }

    /**
     * Minor version of the app.
     * Must be greater than or equal to 0 and less than [MAX_VERSION].
     */
    var patch: Int? = null
        set(value) {
            require(value in 0 until MAX_VERSION) {
                "Patch version must be greater than or equal to 0 and less than $MAX_VERSION."
            }
            field = value
        }

    /**
     * An optional build number appended to the app's version name.
     * Must be greater than or equal to 0.
     */
    var buildNumber: Int? = null
        set(value) {
            if (value != null) {
                require(value >= 0) {
                    "Build number must be greater than or equal to 0."
                }
                field = value
            }
        }
}

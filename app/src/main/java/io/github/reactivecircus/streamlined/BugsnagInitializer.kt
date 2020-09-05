package io.github.reactivecircus.streamlined

import android.content.Context
import com.bugsnag.android.Bugsnag
import com.bugsnag.android.Configuration
import io.github.reactivecircus.bugsnag.BugsnagTree
import radiography.Radiography
import radiography.ScanScopes

fun Context.initializeBugsnag(bugsnagTree: BugsnagTree) {
    val config = Configuration.load(this).apply {
        enabledReleaseStages = setOf(BuildConfig.BUILD_TYPE)
        enabledErrorTypes.ndkCrashes = false
        addOnError { event ->
            val viewHierarchy = Radiography.scan(
                scanScope = ScanScopes.FocusedWindowScope
            )
            event.addMetadata("diagnostic", "view-hierarchy", viewHierarchy)
            bugsnagTree.update(event)
            true
        }
    }
    Bugsnag.start(this, config)
}

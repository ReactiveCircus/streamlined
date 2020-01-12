package io.github.reactivecircus.streamlined

import timber.log.Timber

/**
 * Custom Timber debug tree with line number in the tag.
 */
class DebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String? {
        return super.createStackElementTag(element) + ":" + element.lineNumber
    }
}

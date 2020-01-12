package io.github.reactivecircus.streamlined.testing

import timber.log.Timber

// TODO replace with LogcatTree once Timber multiplatform is released
/**
 * Custom Timber debug tree with line number in the tag.
 */
class TestDebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String? {
        return super.createStackElementTag(element) + ":" + element.lineNumber
    }
}

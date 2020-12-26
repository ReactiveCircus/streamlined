package io.github.reactivecircus.streamlined.navigator

import androidx.annotation.IdRes

sealed class NavControllerType {
    /**
     * Navigate to a root-level destination within nested navigation graphs.
     */
    object Root : NavControllerType()

    /**
     * Navigate to a sibling destination within nested navigation graphs.
     */
    object Parent : NavControllerType()

    /**
     * Navigate to a child destination within nested navigation graphs.
     */
    data class Child(@IdRes val navHostFragmentId: Int) : NavControllerType()
}

package io.github.reactivecircus.streamlined.testing

import io.github.reactivecircus.streamlined.navigator.Navigator
import io.github.reactivecircus.streamlined.navigator.NavigatorProvider
import javax.inject.Inject

/**
 * Implementation of [NavigatorProvider] that provides an no-op [Navigator].
 */
class NoOpNavigatorProvider @Inject constructor() : NavigatorProvider {

    override fun get(): Navigator? = NoOpNavigator
}

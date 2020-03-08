package io.github.reactivecircus.streamlined.navigator

/**
 * Provider of [Navigator].
 */
interface NavigatorProvider {

    fun get(): Navigator?
}

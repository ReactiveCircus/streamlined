package io.github.reactivecircus.streamlined.ui.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject
import javax.inject.Provider

/**
 * A [FragmentFactory] that instantiates [Fragment] using Dagger map multi-binding.
 */
class DaggerFragmentFactory @Inject constructor(
    private val providers: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentClass = loadFragmentClass(classLoader, className)
        val provider = providers[fragmentClass]
        return provider?.get() ?: super.instantiate(classLoader, className)
    }
}

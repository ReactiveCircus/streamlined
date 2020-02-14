package io.github.reactivecircus.streamlined.settings.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.multibindings.IntoMap
import io.github.reactivecircus.streamlined.navigator.NavigatorProvider
import io.github.reactivecircus.streamlined.settings.SettingsFragment
import io.github.reactivecircus.streamlined.testing.NoOpNavigatorProvider
import io.github.reactivecircus.streamlined.ui.di.DynamicFragmentFactory
import io.github.reactivecircus.streamlined.ui.di.FragmentKey

@Module
abstract class SettingsTestAppModule {

    @Binds
    @Reusable
    abstract fun fragmentFactory(impl: DynamicFragmentFactory): FragmentFactory

    @Binds
    @Reusable
    abstract fun navigatorProvider(impl: NoOpNavigatorProvider): NavigatorProvider

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    abstract fun settingsFragment(fragment: SettingsFragment): Fragment
}

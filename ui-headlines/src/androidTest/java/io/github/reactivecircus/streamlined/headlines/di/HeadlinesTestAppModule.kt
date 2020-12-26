package io.github.reactivecircus.streamlined.headlines.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.multibindings.IntoMap
import io.github.reactivecircus.streamlined.headlines.HeadlinesFragment
import io.github.reactivecircus.streamlined.ui.di.DynamicFragmentFactory
import io.github.reactivecircus.streamlined.ui.di.FragmentKey

@Module
abstract class HeadlinesTestAppModule {

    @Binds
    @Reusable
    abstract fun fragmentFactory(impl: DynamicFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(HeadlinesFragment::class)
    abstract fun headlinesFragment(fragment: HeadlinesFragment): Fragment
}

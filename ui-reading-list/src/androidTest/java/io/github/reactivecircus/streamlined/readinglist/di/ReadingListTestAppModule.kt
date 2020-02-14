package io.github.reactivecircus.streamlined.readinglist.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.multibindings.IntoMap
import io.github.reactivecircus.streamlined.navigator.NavigatorProvider
import io.github.reactivecircus.streamlined.readinglist.ReadingListFragment
import io.github.reactivecircus.streamlined.testing.NoOpNavigatorProvider
import io.github.reactivecircus.streamlined.ui.di.DynamicFragmentFactory
import io.github.reactivecircus.streamlined.ui.di.FragmentKey

@Module
abstract class ReadingListTestAppModule {

    @Binds
    @Reusable
    abstract fun fragmentFactory(impl: DynamicFragmentFactory): FragmentFactory

    @Binds
    @Reusable
    abstract fun navigatorProvider(impl: NoOpNavigatorProvider): NavigatorProvider

    @Binds
    @IntoMap
    @FragmentKey(ReadingListFragment::class)
    abstract fun readingListFragment(fragment: ReadingListFragment): Fragment
}

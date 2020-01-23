package io.github.reactivecircus.streamlined.readinglist.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.multibindings.IntoMap
import io.github.reactivecircus.streamlined.navigator.Navigator
import io.github.reactivecircus.streamlined.readinglist.ReadingListFragment
import io.github.reactivecircus.streamlined.testing.NoOpNavigator
import io.github.reactivecircus.streamlined.ui.di.DaggerFragmentFactory
import io.github.reactivecircus.streamlined.ui.di.FragmentKey

@Module
abstract class ReadingListTestAppModule {

    @Binds
    @Reusable
    abstract fun navigator(impl: NoOpNavigator): Navigator

    @Binds
    @Reusable
    abstract fun fragmentFactory(impl: DaggerFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(ReadingListFragment::class)
    abstract fun readingListFragment(fragment: ReadingListFragment): Fragment
}

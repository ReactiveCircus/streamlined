package io.github.reactivecircus.streamlined.di

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.multibindings.IntoMap
import io.github.reactivecircus.streamlined.headlines.HeadlinesFragment
import io.github.reactivecircus.streamlined.home.DefaultHomeUiConfigs
import io.github.reactivecircus.streamlined.home.HomeFragment
import io.github.reactivecircus.streamlined.home.HomeUiConfigs
import io.github.reactivecircus.streamlined.readinglist.ReadingListFragment
import io.github.reactivecircus.streamlined.settings.SettingsFragment
import io.github.reactivecircus.streamlined.storydetails.StoryDetailsFragment
import io.github.reactivecircus.streamlined.ui.di.FragmentKey

@Module
abstract class FeatureModule {

    @Binds
    @Reusable
    abstract fun homeUiConfigs(impl: DefaultHomeUiConfigs): HomeUiConfigs

    @Binds
    @IntoMap
    @FragmentKey(HomeFragment::class)
    abstract fun homeFragment(fragment: HomeFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(HeadlinesFragment::class)
    abstract fun headlinesFragment(fragment: HeadlinesFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ReadingListFragment::class)
    abstract fun readingListFragment(fragment: ReadingListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    abstract fun settingsFragment(fragment: SettingsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(StoryDetailsFragment::class)
    abstract fun storyDetailsFragment(fragment: StoryDetailsFragment): Fragment
}

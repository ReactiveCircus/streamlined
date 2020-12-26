package io.github.reactivecircus.streamlined.storydetails.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.multibindings.IntoMap
import io.github.reactivecircus.streamlined.storydetails.StoryDetailsFragment
import io.github.reactivecircus.streamlined.ui.di.DynamicFragmentFactory
import io.github.reactivecircus.streamlined.ui.di.FragmentKey

@Module
abstract class StoryDetailsTestAppModule {

    @Binds
    @Reusable
    abstract fun fragmentFactory(impl: DynamicFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(StoryDetailsFragment::class)
    abstract fun storyDetailsFragment(fragment: StoryDetailsFragment): Fragment
}

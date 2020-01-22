package io.github.reactivecircus.streamlined.di

import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
abstract class FragmentFactoryModule {

    @Binds
    @Reusable
    abstract fun fragmentFactory(impl: DaggerFragmentFactory): FragmentFactory
}

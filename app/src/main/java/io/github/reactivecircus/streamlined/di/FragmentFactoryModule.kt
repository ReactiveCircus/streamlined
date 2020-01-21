package io.github.reactivecircus.streamlined.di

import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module

@Module
abstract class FragmentFactoryModule {

    @Binds
    abstract fun fragmentFactory(impl: DaggerFragmentFactory): FragmentFactory
}

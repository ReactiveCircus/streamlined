package io.github.reactivecircus.streamlined.ui.home.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.reactivecircus.streamlined.ui.home.DefaultHomeUiConfigs
import io.github.reactivecircus.streamlined.ui.home.HomeUiConfigs

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class HomeModule {

    @Binds
    @ViewModelScoped
    abstract fun homeUiConfigs(impl: DefaultHomeUiConfigs): HomeUiConfigs
}

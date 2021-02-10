package io.github.reactivecircus.streamlined.home.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn
import io.github.reactivecircus.streamlined.home.HomeUiConfigs
import io.github.reactivecircus.streamlined.home.TestHomeUiConfigs

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [HomeModule::class],
)
abstract class TestHomeModule {

    @Binds
    @Reusable
    abstract fun homeUiConfigs(impl: TestHomeUiConfigs): HomeUiConfigs
}

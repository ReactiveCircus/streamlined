package io.github.reactivecircus.streamlined.di

import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.Reusable
import io.github.reactivecircus.streamlined.StreamlinedNavigatorProvider
import io.github.reactivecircus.streamlined.navigator.NavigatorProvider
import io.github.reactivecircus.streamlined.ui.di.DynamicFragmentFactory
import io.github.reactivecircus.streamlined.work.di.ScheduledTasksModule

@Module(
    includes = [
        FeatureModule::class,
        ScheduledTasksModule::class
    ]
)
abstract class IntegrationTestAppModule {

    @Binds
    @Reusable
    abstract fun fragmentFactory(impl: DynamicFragmentFactory): FragmentFactory

    @Binds
    @Reusable
    abstract fun navigatorProvider(impl: StreamlinedNavigatorProvider): NavigatorProvider
}

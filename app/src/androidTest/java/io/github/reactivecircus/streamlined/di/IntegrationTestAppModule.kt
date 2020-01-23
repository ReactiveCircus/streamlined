package io.github.reactivecircus.streamlined.di

import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.Reusable
import io.github.reactivecircus.streamlined.StreamlinedNavigator
import io.github.reactivecircus.streamlined.navigator.Navigator
import io.github.reactivecircus.streamlined.ui.di.DaggerFragmentFactory
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
    abstract fun navigator(impl: StreamlinedNavigator): Navigator

    @Binds
    @Reusable
    abstract fun fragmentFactory(impl: DaggerFragmentFactory): FragmentFactory
}

package io.github.reactivecircus.streamlined.di

import dagger.Binds
import dagger.Module
import io.github.reactivecircus.streamlined.StreamlinedNavigator
import io.github.reactivecircus.streamlined.navigator.Navigator

@Module
abstract class AppModule {

    @Binds
    abstract fun navigator(impl: StreamlinedNavigator): Navigator
}

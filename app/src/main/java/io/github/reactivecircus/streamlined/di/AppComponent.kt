package io.github.reactivecircus.streamlined.di

import android.app.Application
import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import io.github.reactivecircus.analytics.AnalyticsApi
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        FragmentFactoryModule::class,
        FeatureModule::class,
        ServiceModule::class,
        RepositoryModule::class,
        SdkModule::class
    ]
)
interface AppComponent {

    val fragmentFactory: FragmentFactory

    val analyticsApi: AnalyticsApi

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    companion object {
        fun factory(): Factory = DaggerAppComponent.factory()
    }
}

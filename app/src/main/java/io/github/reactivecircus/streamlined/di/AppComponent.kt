package io.github.reactivecircus.streamlined.di

import android.content.Context
import androidx.fragment.app.FragmentFactory
import androidx.work.Configuration
import dagger.BindsInstance
import dagger.Component
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.work.di.BackgroundWorkModule
import io.github.reactivecircus.streamlined.work.scheduler.BackgroundWorkScheduler
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        FeatureModule::class,
        BackgroundWorkModule::class,
        ServiceModule::class,
        RepositoryModule::class,
        SdkModule::class
    ]
)
interface AppComponent {

    val fragmentFactory: FragmentFactory

    val analyticsApi: AnalyticsApi

    val workManagerConfiguration: Configuration

    val backgroundWorkScheduler: BackgroundWorkScheduler

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    companion object {
        fun factory(): Factory = DaggerAppComponent.factory()
    }
}

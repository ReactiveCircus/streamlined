package io.github.reactivecircus.streamlined.di

import android.content.Context
import androidx.fragment.app.FragmentFactory
import androidx.work.Configuration
import coil.ImageLoader
import dagger.BindsInstance
import dagger.Component
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.ScreenNameNotifier
import io.github.reactivecircus.streamlined.navigator.NavigatorProvider
import io.github.reactivecircus.streamlined.work.di.ScheduledTasksModule
import io.github.reactivecircus.streamlined.work.scheduler.TaskScheduler
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        FeatureModule::class,
        ScheduledTasksModule::class,
        ServiceModule::class,
        RepositoryModule::class,
        SdkModule::class
    ]
)
interface AppComponent {

    val fragmentFactory: FragmentFactory

    val navigatorProvider: NavigatorProvider

    val analyticsApi: AnalyticsApi

    val screenNameNotifier: ScreenNameNotifier

    val imageLoader: ImageLoader

    val workManagerConfiguration: Configuration

    val taskScheduler: TaskScheduler

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    companion object {
        fun factory(): Factory = DaggerAppComponent.factory()
    }
}

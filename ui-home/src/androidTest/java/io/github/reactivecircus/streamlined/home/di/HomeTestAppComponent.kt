package io.github.reactivecircus.streamlined.home.di

import androidx.fragment.app.FragmentFactory
import androidx.test.core.app.ApplicationProvider
import dagger.Component
import io.github.reactivecircus.streamlined.testing.di.TestScope
import io.github.reactivecircus.streamlined.testing.di.TestingFrameworkComponent

@TestScope
@Component(
    modules = [HomeTestAppModule::class],
    dependencies = [TestingFrameworkComponent::class]
)
interface HomeTestAppComponent {

    val fragmentFactory: FragmentFactory

    @Component.Factory
    interface Factory {
        fun create(testingFrameworkComponent: TestingFrameworkComponent): HomeTestAppComponent
    }

    companion object {
        private val instance: HomeTestAppComponent by lazy {
            DaggerHomeTestAppComponent.factory().create(
                TestingFrameworkComponent.getOrCreate(
                    ApplicationProvider.getApplicationContext()
                )
            )
        }

        fun getOrCreate(): HomeTestAppComponent = instance
    }
}

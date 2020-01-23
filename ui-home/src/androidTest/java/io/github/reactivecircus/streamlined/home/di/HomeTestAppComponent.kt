package io.github.reactivecircus.streamlined.home.di

import androidx.fragment.app.FragmentFactory
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
        fun factory(): Factory = DaggerHomeTestAppComponent.factory()
    }
}

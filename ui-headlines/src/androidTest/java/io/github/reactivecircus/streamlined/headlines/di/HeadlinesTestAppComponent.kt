package io.github.reactivecircus.streamlined.headlines.di

import androidx.fragment.app.FragmentFactory
import dagger.Component
import io.github.reactivecircus.streamlined.testing.di.TestScope
import io.github.reactivecircus.streamlined.testing.di.TestingFrameworkComponent

@TestScope
@Component(
    modules = [HeadlinesTestAppModule::class],
    dependencies = [TestingFrameworkComponent::class]
)
interface HeadlinesTestAppComponent {

    val fragmentFactory: FragmentFactory

    @Component.Factory
    interface Factory {
        fun create(testingFrameworkComponent: TestingFrameworkComponent): HeadlinesTestAppComponent
    }

    companion object {
        fun factory(): Factory = DaggerHeadlinesTestAppComponent.factory()
    }
}

package io.github.reactivecircus.streamlined.readinglist.di

import androidx.fragment.app.FragmentFactory
import dagger.Component
import io.github.reactivecircus.streamlined.testing.di.TestScope
import io.github.reactivecircus.streamlined.testing.di.TestingFrameworkComponent

@TestScope
@Component(
    modules = [ReadingListTestAppModule::class],
    dependencies = [TestingFrameworkComponent::class]
)
interface ReadingListTestAppComponent {

    val fragmentFactory: FragmentFactory

    @Component.Factory
    interface Factory {
        fun create(testingFrameworkComponent: TestingFrameworkComponent): ReadingListTestAppComponent
    }

    companion object {
        fun factory(): Factory = DaggerReadingListTestAppComponent.factory()
    }
}

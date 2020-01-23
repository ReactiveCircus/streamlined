package io.github.reactivecircus.streamlined.storydetails.di

import androidx.fragment.app.FragmentFactory
import dagger.Component
import io.github.reactivecircus.streamlined.storydetails.StoryDetailsAssistedModule
import io.github.reactivecircus.streamlined.testing.di.TestScope
import io.github.reactivecircus.streamlined.testing.di.TestingFrameworkComponent

@TestScope
@Component(
    modules = [
        StoryDetailsTestAppModule::class,
        StoryDetailsAssistedModule::class
    ],
    dependencies = [TestingFrameworkComponent::class]
)
interface StoryDetailsTestAppComponent {

    val fragmentFactory: FragmentFactory

    @Component.Factory
    interface Factory {
        fun create(testingFrameworkComponent: TestingFrameworkComponent): StoryDetailsTestAppComponent
    }

    companion object {
        fun factory(): Factory = DaggerStoryDetailsTestAppComponent.factory()
    }
}

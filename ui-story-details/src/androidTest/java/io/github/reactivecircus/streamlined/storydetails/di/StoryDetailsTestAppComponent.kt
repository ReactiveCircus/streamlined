package io.github.reactivecircus.streamlined.storydetails.di

import androidx.fragment.app.FragmentFactory
import androidx.test.core.app.ApplicationProvider
import dagger.Component
import io.github.reactivecircus.streamlined.testing.di.TestScope
import io.github.reactivecircus.streamlined.testing.di.TestingFrameworkComponent

@TestScope
@Component(
    modules = [StoryDetailsTestAppModule::class],
    dependencies = [TestingFrameworkComponent::class]
)
interface StoryDetailsTestAppComponent {

    val fragmentFactory: FragmentFactory

    @Component.Factory
    interface Factory {
        fun create(testingFrameworkComponent: TestingFrameworkComponent): StoryDetailsTestAppComponent
    }

    companion object {
        private val instance: StoryDetailsTestAppComponent by lazy {
            DaggerStoryDetailsTestAppComponent.factory().create(
                TestingFrameworkComponent.getOrCreate(
                    ApplicationProvider.getApplicationContext()
                )
            )
        }

        fun getOrCreate(): StoryDetailsTestAppComponent = instance
    }
}

package io.github.reactivecircus.streamlined.settings.di

import androidx.fragment.app.FragmentFactory
import androidx.test.core.app.ApplicationProvider
import dagger.Component
import io.github.reactivecircus.streamlined.testing.di.TestScope
import io.github.reactivecircus.streamlined.testing.di.TestingFrameworkComponent

@TestScope
@Component(
    modules = [SettingsTestAppModule::class],
    dependencies = [TestingFrameworkComponent::class]
)
interface SettingsTestAppComponent {

    val fragmentFactory: FragmentFactory

    @Component.Factory
    interface Factory {
        fun create(testingFrameworkComponent: TestingFrameworkComponent): SettingsTestAppComponent
    }

    companion object {
        private val instance: SettingsTestAppComponent by lazy {
            DaggerSettingsTestAppComponent.factory().create(
                TestingFrameworkComponent.getOrCreate(
                    ApplicationProvider.getApplicationContext()
                )
            )
        }

        fun getOrCreate(): SettingsTestAppComponent = instance
    }
}

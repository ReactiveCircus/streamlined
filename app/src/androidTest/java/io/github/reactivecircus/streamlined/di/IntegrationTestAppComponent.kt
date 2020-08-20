package io.github.reactivecircus.streamlined.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.reactivecircus.streamlined.testing.di.TestScope
import io.github.reactivecircus.streamlined.testing.di.TestingFrameworkComponent

@TestScope
@Component(
    modules = [IntegrationTestAppModule::class],
    dependencies = [TestingFrameworkComponent::class]
)
interface IntegrationTestAppComponent : AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            testingFrameworkComponent: TestingFrameworkComponent,
        ): IntegrationTestAppComponent
    }

    companion object {
        fun factory(): Factory = DaggerIntegrationTestAppComponent.factory()
    }
}

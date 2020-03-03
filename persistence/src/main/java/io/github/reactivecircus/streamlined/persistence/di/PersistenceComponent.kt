package io.github.reactivecircus.streamlined.persistence.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.reactivecircus.streamlined.persistence.StoryDao
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
@Component(
    modules = [PersistenceModule::class]
)
interface PersistenceComponent {

    val storyDao: StoryDao

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance coroutineContext: CoroutineContext,
            @BindsInstance databaseName: String?
        ): PersistenceComponent
    }

    companion object {
        fun factory(): Factory = DaggerPersistenceComponent.factory()
    }
}

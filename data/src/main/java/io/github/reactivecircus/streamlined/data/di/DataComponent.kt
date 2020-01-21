package io.github.reactivecircus.streamlined.data.di

import android.content.Context
import com.dropbox.android.external.store4.Store
import dagger.BindsInstance
import dagger.Component
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.BookmarkRepository
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DataModule::class]
)
interface DataComponent {

    val storyStore: Store<Unit, List<Story>>

    val bookmarkRepository: BookmarkRepository

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance newsApiService: NewsApiService
        ): DataComponent
    }

    companion object {
        fun factory(): Factory = DaggerDataComponent.factory()
    }
}

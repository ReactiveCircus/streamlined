package io.github.reactivecircus.streamlined.storydetails

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_StoryDetailsAssistedModule::class])
interface StoryDetailsAssistedModule

package io.github.reactivecircus.streamlined.remote.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
internal annotation class BaseUrl

@Retention(AnnotationRetention.BINARY)
@Qualifier
internal annotation class ApiKey

package io.github.reactivecircus.streamlined.remote.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import io.github.reactivecircus.streamlined.remote.AuthInterceptor
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
internal object RealRemoteModule {

    @Provides
    @Singleton
    fun okHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            // add logging interceptor
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun retrofit(
        @BaseUrl baseUrl: String,
        okhttpClient: Lazy<OkHttpClient>
    ): Retrofit {
        val contentType = "application/json; charset=utf-8".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .callFactory(
                object : Call.Factory {
                    override fun newCall(request: Request): Call {
                        return okhttpClient.get().newCall(request)
                    }
                }
            )
            .addConverterFactory(
                Json { ignoreUnknownKeys = true }.asConverterFactory(contentType)
            )
            .build()
    }

    @Provides
    @Singleton
    fun newsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create()
    }
}

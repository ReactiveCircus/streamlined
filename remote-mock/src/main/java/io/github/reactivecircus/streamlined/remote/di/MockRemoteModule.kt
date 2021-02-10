@file:Suppress("MagicNumber")

package io.github.reactivecircus.streamlined.remote.di

import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.reactivecircus.streamlined.remote.api.MockNewsApiService
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockRemoteModule {

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun retrofit(okhttpClient: Lazy<OkHttpClient>): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DUMMY_URL)
            .callFactory { request -> okhttpClient.get().newCall(request) }
            .build()
    }

    @Provides
    @Singleton
    fun networkBehavior(): NetworkBehavior {
        return NetworkBehavior.create().apply {
            // make sure behavior is deterministic
            setVariancePercent(0)
            // 200 ms delay
            setDelay(200, TimeUnit.MILLISECONDS)
            // 5% failure
            setFailurePercent(5)
        }
    }

    @Provides
    @Singleton
    fun newsApiService(
        networkBehavior: NetworkBehavior,
        retrofit: Retrofit
    ): NewsApiService {
        return MockNewsApiService(
            delegate = MockRetrofit.Builder(retrofit)
                .apply { networkBehavior(networkBehavior) }
                .build().create(NewsApiService::class.java)
        )
    }
}

private const val MOCK_SERVER_PORT = 5_000
private const val DUMMY_URL = "http://localhost:$MOCK_SERVER_PORT/"

@file:Suppress("MagicNumber")

package io.github.reactivecircus.streamlined.remote.di

import dagger.Lazy
import dagger.Module
import dagger.Provides
import io.github.reactivecircus.streamlined.remote.api.MockNewsApiService
import io.github.reactivecircus.streamlined.remote.api.NewsApiService
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

private const val MOCK_SERVER_PORT = 5_000
private const val DUMMY_URL = "http://localhost:$MOCK_SERVER_PORT/"

@Module
internal object MockRemoteModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }

    @Provides
    fun provideRetrofit(okhttpClient: Lazy<OkHttpClient>): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DUMMY_URL)
            .callFactory(object : Call.Factory {
                override fun newCall(request: Request): Call {
                    return okhttpClient.get().newCall(request)
                }
            })
            .build()
    }

    @Provides
    fun provideNetworkBehavior(): NetworkBehavior {
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
    fun provideNewsApiService(
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

package io.github.reactivecircus.streamlined.remote

import io.github.reactivecircus.streamlined.remote.di.ApiKey
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * An [Interceptor] that adds the [apiKey] as an HTTP header for each request.
 */
internal class AuthInterceptor @Inject constructor(
    @ApiKey private val apiKey: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader(API_KEY_HEADER_NAME, apiKey)
            .build()
        return chain.proceed(newRequest)
    }

    companion object {
        const val API_KEY_HEADER_NAME = "X-Api-Key"
    }
}

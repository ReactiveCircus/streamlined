package io.github.reactivecircus.streamlined.remote

import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An [Interceptor] that adds the [apiKey] as an HTTP header for each request.
 */
internal class AuthInterceptor @Inject constructor(
    private val apiConfigs: ApiConfigs
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader(API_KEY_HEADER_NAME, apiConfigs.apiKey)
            .build()
        return chain.proceed(newRequest)
    }

    companion object {
        const val API_KEY_HEADER_NAME = "X-Api-Key"
    }
}

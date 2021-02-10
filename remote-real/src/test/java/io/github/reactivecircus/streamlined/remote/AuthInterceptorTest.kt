package io.github.reactivecircus.streamlined.remote

import com.google.common.truth.Truth.assertThat
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class AuthInterceptorTest {

    private val apiKey = "abc"

    private val server = MockWebServer()

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(
            AuthInterceptor(
                ApiConfigs(apiKey = apiKey, baseUrl = "url")
            )
        )
    }.build()

    @Before
    fun setUp() {
        // start mock server
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `API key is injected as HTTP header`() {
        server.enqueue(MockResponse())
        val request = Request.Builder()
            .url(server.url("/"))
            .build()

        client.newCall(request).execute()

        val recordedRequest = server.takeRequest()

        val injectedApiKey = recordedRequest.getHeader(AuthInterceptor.API_KEY_HEADER_NAME)

        assertThat(injectedApiKey).isEqualTo(apiKey)
    }
}

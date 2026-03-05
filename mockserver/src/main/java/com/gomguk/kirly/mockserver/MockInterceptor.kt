package com.gomguk.kirly.mockserver

import android.os.SystemClock
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject
import kotlin.random.Random

class MockInterceptor @Inject constructor(
    private val mockServer: MockServer
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        SystemClock.sleep(Random.nextInt(1, 3) * 1000L)

        val responseString = mockServer.get(chain.request())

        return if (responseString == null) {
            chain.proceed(chain.request())
                .newBuilder()
                .code(500)
                .message("에러 발생")
                .build()
        } else {
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .code(200)
                .message(responseString)
                .body(responseString.toByteArray().toResponseBody("application/json".toMediaType()))
                .addHeader("content-type", "application/json")
                .build()
        }
    }
}

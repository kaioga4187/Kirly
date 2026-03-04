package com.gomguk.kirly.data.repository

import com.gomguk.kirly.data.SectionInfo
import com.gomguk.kirly.data.SectionsResponse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {
    fun getSections(page: Int = 1): List<SectionInfo> {
        // MockInterceptor가 적용된 okHttpClient를 사용하여 요청을 보냅니다.
        val request = Request.Builder()
            .url("https://kurly.com/sections?page=$page")
            .build()

        // 실제 네트워크 통신 로직이 들어갈 자리입니다. 
        // 현재는 MockInterceptor가 mock 응답을 반환합니다.
        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return emptyList()
            
            val responseBody = response.body?.string()
            return if (responseBody != null) {
                try {
                    val sectionsResponse = gson.fromJson(responseBody, SectionsResponse::class.java)
                    sectionsResponse.data
                } catch (_: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }
}

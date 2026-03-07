package com.gomguk.kurly.data.repository

import com.gomguk.kurly.data.Product
import com.gomguk.kurly.data.ProductResponse
import com.gomguk.kurly.data.SectionInfo
import com.gomguk.kurly.data.SectionsResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {
    suspend fun getSections(page: Int = 1): List<SectionInfo> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://kurly.com/sections?page=$page")
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return@withContext emptyList()

            val responseBody = response.body?.string()
            if (responseBody != null) {
                try {
                    val sectionsResponse = gson.fromJson(responseBody, SectionsResponse::class.java)
                    sectionsResponse.data
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }

    suspend fun getSectionItems(sectionId: Int): List<Product> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://kurly.com/section/products?sectionId=$sectionId")
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return@withContext emptyList()

            val responseBody = response.body?.string()
            if (responseBody != null) {
                try {
                    val productResponse = gson.fromJson(responseBody, ProductResponse::class.java)
                    productResponse.data
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }
}

package com.gomguk.kurly.data.repository

import com.gomguk.kurly.data.Product
import com.gomguk.kurly.data.SectionsResponse
import com.gomguk.kurly.data.api.KurlyApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val apiService: KurlyApiService
) {
    suspend fun getSections(page: Int = 1): SectionsResponse? {
        return try {
            val response = apiService.getSections(page)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getSectionItems(sectionId: Int): List<Product> {
        return try {
            val response = apiService.getSectionItems(sectionId)
            if (response.isSuccessful) {
                response.body()?.data ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

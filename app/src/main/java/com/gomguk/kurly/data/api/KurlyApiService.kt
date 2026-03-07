package com.gomguk.kurly.data.api

import com.gomguk.kurly.data.ProductResponse
import com.gomguk.kurly.data.SectionsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KurlyApiService {
    @GET("sections")
    suspend fun getSections(
        @Query("page") page: Int
    ): Response<SectionsResponse>

    @GET("section/products")
    suspend fun getSectionItems(
        @Query("sectionId") sectionId: Int
    ): Response<ProductResponse>
}

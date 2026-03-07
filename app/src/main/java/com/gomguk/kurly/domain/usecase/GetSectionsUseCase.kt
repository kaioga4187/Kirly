package com.gomguk.kurly.domain.usecase

import com.gomguk.kurly.data.SectionsResponse
import com.gomguk.kurly.data.local.dao.FavoriteProductDao
import com.gomguk.kurly.data.repository.MainRepository
import javax.inject.Inject

class GetSectionsUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val favoriteProductDao: FavoriteProductDao
) {
    suspend operator fun invoke(page: Int = 1): SectionsResponse? {
        val response = mainRepository.getSections(page) ?: return null
        val favoriteIds = favoriteProductDao.getFavoriteIdList()

        response.data.forEach { section ->
            section.products?.let { products ->
                section.products = products.map { product ->
                    product.copy(isFavorite = favoriteIds.contains(product.id))
                }
            }
        }
        return response
    }
}

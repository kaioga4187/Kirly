package com.gomguk.kurly.domain.usecase

import com.gomguk.kurly.data.Product
import com.gomguk.kurly.data.local.dao.FavoriteProductDao
import com.gomguk.kurly.data.repository.MainRepository
import javax.inject.Inject

class GetSectionItemsUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val favoriteProductDao: FavoriteProductDao
) {
    suspend operator fun invoke(sectionId: Int): List<Product> {
        val products = mainRepository.getSectionItems(sectionId)
        val favoriteIds = favoriteProductDao.getFavoriteIdList()
        
        return products.map { product ->
            product.copy(isFavorite = favoriteIds.contains(product.id))
        }
    }
}

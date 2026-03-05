package com.gomguk.kirly.domain.usecase

import com.gomguk.kirly.data.Product
import com.gomguk.kirly.data.local.dao.FavoriteProductDao
import com.gomguk.kirly.data.repository.MainRepository
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

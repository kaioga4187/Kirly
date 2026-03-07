package com.gomguk.kurly.domain.usecase

import com.gomguk.kurly.data.SectionInfo
import com.gomguk.kurly.data.local.dao.FavoriteProductDao
import com.gomguk.kurly.data.repository.MainRepository
import javax.inject.Inject

class GetSectionsUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val favoriteProductDao: FavoriteProductDao
) {
    suspend operator fun invoke(page: Int = 1): List<SectionInfo> {
        val sections = mainRepository.getSections(page)
        val favoriteIds = favoriteProductDao.getFavoriteIdList()

        sections.forEach { section ->
            section.products?.let { products ->
                section.products = products.map { product ->
                    product.copy(isFavorite = favoriteIds.contains(product.id))
                }
            }
        }
        return sections
    }
}

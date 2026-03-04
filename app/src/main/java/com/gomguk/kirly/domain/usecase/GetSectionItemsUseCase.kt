package com.gomguk.kirly.domain.usecase

import com.gomguk.kirly.data.Product
import com.gomguk.kirly.data.repository.MainRepository
import javax.inject.Inject

class GetSectionItemsUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    suspend operator fun invoke(sectionId: Int): List<Product> = mainRepository.getSectionItems(sectionId)
}

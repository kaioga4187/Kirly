package com.gomguk.kirly.domain.usecase

import com.gomguk.kirly.data.SectionInfo
import com.gomguk.kirly.data.repository.MainRepository
import javax.inject.Inject

class GetSectionsUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    suspend operator fun invoke(page: Int = 1): List<SectionInfo> = mainRepository.getSections(page)
}

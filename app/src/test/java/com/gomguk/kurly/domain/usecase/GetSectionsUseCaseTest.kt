package com.gomguk.kurly.domain.usecase

import com.gomguk.kurly.data.Product
import com.gomguk.kurly.data.SectionInfo
import com.gomguk.kurly.data.SectionType
import com.gomguk.kurly.data.SectionsResponse
import com.gomguk.kurly.data.local.dao.FavoriteProductDao
import com.gomguk.kurly.data.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GetSectionsUseCaseTest {

    @Mock
    private lateinit var mainRepository: MainRepository

    @Mock
    private lateinit var favoriteProductDao: FavoriteProductDao

    private lateinit var getSectionsUseCase: GetSectionsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getSectionsUseCase = GetSectionsUseCase(mainRepository, favoriteProductDao)
    }

    @Test
    fun `invoke should map isFavorite correctly based on local database`() = runTest {
        // Given
        val productId = 100
        val mockProducts = listOf(
            Product(id = productId, name = "Test Product", image = "url", originalPrice = 1000)
        )
        val mockSection = SectionInfo("Title", 1, SectionType.Vertical, "url", mockProducts)
        val mockResponse = SectionsResponse(data = listOf(mockSection), paging = null)

        whenever(mainRepository.getSections(1)).thenReturn(mockResponse)
        whenever(favoriteProductDao.getFavoriteIdList()).thenReturn(listOf(productId))

        // When
        val result = getSectionsUseCase(1)

        // Then
        val product = result?.data?.first()?.products?.first()
        assertEquals(true, product?.isFavorite)
    }

    @Test
    fun `invoke should return null when repository returns null`() = runTest {
        // Given
        whenever(mainRepository.getSections(1)).thenReturn(null)

        // When
        val result = getSectionsUseCase(1)

        // Then
        assertEquals(null, result)
    }
}

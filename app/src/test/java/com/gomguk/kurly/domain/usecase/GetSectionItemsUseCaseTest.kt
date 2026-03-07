package com.gomguk.kurly.domain.usecase

import com.gomguk.kurly.data.Product
import com.gomguk.kurly.data.local.dao.FavoriteProductDao
import com.gomguk.kurly.data.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GetSectionItemsUseCaseTest {

    @Mock
    private lateinit var mainRepository: MainRepository

    @Mock
    private lateinit var favoriteProductDao: FavoriteProductDao

    private lateinit var getSectionItemsUseCase: GetSectionItemsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getSectionItemsUseCase = GetSectionItemsUseCase(mainRepository, favoriteProductDao)
    }

    @Test
    fun `invoke should return products with correct favorite state`() = runTest {
        // Given
        val sectionId = 1
        val products = listOf(
            Product(id = 1, name = "Product 1", image = "", originalPrice = 1000),
            Product(id = 2, name = "Product 2", image = "", originalPrice = 2000)
        )
        whenever(mainRepository.getSectionItems(sectionId)).thenReturn(products)
        whenever(favoriteProductDao.getFavoriteIdList()).thenReturn(listOf(1))

        // When
        val result = getSectionItemsUseCase(sectionId)

        // Then
        assertEquals(2, result.size)
        assertEquals(true, result[0].isFavorite)
        assertEquals(false, result[1].isFavorite)
    }
}

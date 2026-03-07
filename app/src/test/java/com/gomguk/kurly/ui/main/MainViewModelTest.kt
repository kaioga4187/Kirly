package com.gomguk.kurly.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.gomguk.kurly.data.Paging
import com.gomguk.kurly.data.Product
import com.gomguk.kurly.data.SectionInfo
import com.gomguk.kurly.data.SectionType
import com.gomguk.kurly.data.SectionsResponse
import com.gomguk.kurly.data.local.dao.FavoriteProductDao
import com.gomguk.kurly.data.local.entity.FavoriteProductEntity
import com.gomguk.kurly.domain.usecase.GetSectionItemsUseCase
import com.gomguk.kurly.domain.usecase.GetSectionsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var getSectionsUseCase: GetSectionsUseCase

    @Mock
    private lateinit var getSectionItemsUseCase: GetSectionItemsUseCase

    @Mock
    private lateinit var favoriteProductDao: FavoriteProductDao

    @Mock
    private lateinit var sectionInfoObserver: Observer<List<SectionInfo?>>

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init calls loadItems and updates sectionInfoList`() = runTest(testDispatcher) {
        // Given
        val mockResponse = SectionsResponse(
            data = listOf(SectionInfo("Title", 1, SectionType.Vertical, "url")),
            paging = Paging(next_page = 2)
        )
        whenever(getSectionsUseCase(1)).thenReturn(mockResponse)

        // When
        viewModel = MainViewModel(getSectionsUseCase, getSectionItemsUseCase, favoriteProductDao)
        viewModel.sectionInfoList.observeForever(sectionInfoObserver)
        advanceUntilIdle()

        // Then
        verify(sectionInfoObserver, atLeastOnce()).onChanged(any())
        assertEquals(1, viewModel.sectionInfoList.value?.filterNotNull()?.size)
    }

    @Test
    fun `loadItems should load next page and append to list`() = runTest(testDispatcher) {
        // Given
        val firstResponse = SectionsResponse(
            data = listOf(SectionInfo("Page 1", 1, SectionType.Vertical, "")),
            paging = Paging(next_page = 2)
        )
        whenever(getSectionsUseCase(1)).thenReturn(firstResponse)
        
        viewModel = MainViewModel(getSectionsUseCase, getSectionItemsUseCase, favoriteProductDao)
        advanceUntilIdle()

        val secondResponse = SectionsResponse(
            data = listOf(SectionInfo("Page 2", 2, SectionType.Vertical, "")),
            paging = null
        )
        whenever(getSectionsUseCase(2)).thenReturn(secondResponse)

        // When
        viewModel.loadItems()
        advanceUntilIdle()

        // Then
        val currentList = viewModel.sectionInfoList.value?.filterNotNull()
        assertEquals(2, currentList?.size)
        assertEquals("Page 2", currentList?.last()?.title)
    }

    @Test
    fun `refresh updates sectionInfoList with new data`() = runTest(testDispatcher) {
        // Given
        whenever(getSectionsUseCase(1)).thenReturn(SectionsResponse(listOf(SectionInfo("Old", 1, SectionType.Vertical, "")), null))
        viewModel = MainViewModel(getSectionsUseCase, getSectionItemsUseCase, favoriteProductDao)
        advanceUntilIdle()

        val newResponse = SectionsResponse(
            data = listOf(SectionInfo("New", 2, SectionType.Horizontal, "url")),
            paging = null
        )
        whenever(getSectionsUseCase(1)).thenReturn(newResponse)

        // When
        viewModel.refresh()
        advanceUntilIdle()

        // Then
        val currentList = viewModel.sectionInfoList.value?.filterNotNull()
        assertEquals(1, currentList?.size)
        assertEquals("New", currentList?.first()?.title)
    }

    @Test
    fun `loadSectionItems updates section products`() = runTest(testDispatcher) {
        // Given
        val sectionId = 1
        val initialSection = SectionInfo("Title", sectionId, SectionType.Vertical, "", products = null)
        whenever(getSectionsUseCase(1)).thenReturn(SectionsResponse(listOf(initialSection), null))
        
        viewModel = MainViewModel(getSectionsUseCase, getSectionItemsUseCase, favoriteProductDao)
        advanceUntilIdle()

        val mockProducts = listOf(Product(1, "Product", "", 1000))
        whenever(getSectionItemsUseCase(sectionId)).thenReturn(mockProducts)

        // When
        viewModel.loadSectionItems(sectionId)
        advanceUntilIdle()

        // Then
        val updatedSection = viewModel.sectionInfoList.value?.filterNotNull()?.find { it.id == sectionId }
        assertEquals(mockProducts, updatedSection?.products)
    }

    @Test
    fun `toggleFavorite inserts favorite when isFavorite is true`() = runTest(testDispatcher) {
        // Given
        whenever(getSectionsUseCase(any())).thenReturn(null)
        viewModel = MainViewModel(getSectionsUseCase, getSectionItemsUseCase, favoriteProductDao)
        val product = Product(id = 123, name = "Test", image = "", originalPrice = 1000, isFavorite = true)

        // When
        viewModel.toggleFavorite(product)
        advanceUntilIdle()

        // Then
        verify(favoriteProductDao).insertFavorite(FavoriteProductEntity(123))
    }

    @Test
    fun `toggleFavorite deletes favorite when isFavorite is false`() = runTest(testDispatcher) {
        // Given
        whenever(getSectionsUseCase(any())).thenReturn(null)
        viewModel = MainViewModel(getSectionsUseCase, getSectionItemsUseCase, favoriteProductDao)
        val product = Product(id = 123, name = "Test", image = "", originalPrice = 1000, isFavorite = false)

        // When
        viewModel.toggleFavorite(product)
        advanceUntilIdle()

        // Then
        verify(favoriteProductDao).deleteFavorite(123)
    }
}

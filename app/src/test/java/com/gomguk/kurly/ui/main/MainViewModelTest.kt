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
        // loadItems adds null first (loading), then data, so it changes at least twice
        verify(sectionInfoObserver, atLeastOnce()).onChanged(any())
        assert(viewModel.sectionInfoList.value?.filterNotNull()?.size == 1)
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
        assert(currentList?.size == 1)
        assert(currentList?.first()?.title == "New")
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

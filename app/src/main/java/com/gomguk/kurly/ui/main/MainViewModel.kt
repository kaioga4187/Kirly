package com.gomguk.kurly.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gomguk.kurly.data.Product
import com.gomguk.kurly.data.SectionInfo
import com.gomguk.kurly.data.local.dao.FavoriteProductDao
import com.gomguk.kurly.data.local.entity.FavoriteProductEntity
import com.gomguk.kurly.domain.usecase.GetSectionItemsUseCase
import com.gomguk.kurly.domain.usecase.GetSectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSectionsUseCase: GetSectionsUseCase,
    private val getSectionItemsUseCase: GetSectionItemsUseCase,
    private val favoriteProductDao: FavoriteProductDao
) : ViewModel() {

    private val _sectionInfoList = MutableLiveData<List<SectionInfo?>>()
    val sectionInfoList: LiveData<List<SectionInfo?>> = _sectionInfoList

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private var nextPage: Int? = 1
    private var isLoading = false

    init {
        loadItems()
    }

    fun refresh() {
        nextPage = 1
        _isRefreshing.value = true
        
        viewModelScope.launch {
            val response = getSectionsUseCase(1)
            if (response != null) {
                _sectionInfoList.value = response.data
                nextPage = response.paging?.next_page
            } else {
                _sectionInfoList.value = emptyList()
                nextPage = null
            }
            _isRefreshing.value = false
        }
    }

    fun loadItems() {
        val pageToLoad = nextPage
        if (isLoading || pageToLoad == null) return
        
        isLoading = true
        
        val currentList = (_sectionInfoList.value ?: emptyList()).toMutableList()
        currentList.add(null)
        _sectionInfoList.value = currentList

        viewModelScope.launch {
            val response = getSectionsUseCase(pageToLoad)
            
            val updatedList = (_sectionInfoList.value ?: emptyList()).toMutableList()
            if (updatedList.isNotEmpty() && updatedList.last() == null) {
                updatedList.removeAt(updatedList.size - 1)
            }

            if (response == null || response.data.isEmpty()) {
                nextPage = null
                _sectionInfoList.value = updatedList
            } else {
                _sectionInfoList.value = updatedList + response.data
                nextPage = response.paging?.next_page
            }

            isLoading = false
        }
    }

    fun loadSectionItems(sectionId: Int) {
        val currentList = _sectionInfoList.value ?: return
        val section = currentList.find { it?.id == sectionId } ?: return

        if (!section.products.isNullOrEmpty()) return

        viewModelScope.launch {
            val products = getSectionItemsUseCase(sectionId)
            section.products = products

            _sectionInfoList.value = currentList
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            if (product.isFavorite) {
                favoriteProductDao.insertFavorite(FavoriteProductEntity(product.id))
            } else {
                favoriteProductDao.deleteFavorite(product.id)
            }
        }
    }
}

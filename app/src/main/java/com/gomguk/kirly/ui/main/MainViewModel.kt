package com.gomguk.kirly.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gomguk.kirly.data.Product
import com.gomguk.kirly.data.SectionInfo
import com.gomguk.kirly.data.local.dao.FavoriteProductDao
import com.gomguk.kirly.data.local.entity.FavoriteProductEntity
import com.gomguk.kirly.domain.usecase.GetSectionItemsUseCase
import com.gomguk.kirly.domain.usecase.GetSectionsUseCase
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

    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false

    init {
        loadItems()
    }

    fun refresh() {
        currentPage = 1
        isLastPage = false
        _isRefreshing.value = true
        
        viewModelScope.launch {
            val newSections = getSectionsUseCase(currentPage)
            _sectionInfoList.value = newSections
            currentPage++
            _isRefreshing.value = false
        }
    }

    fun loadItems() {
        if (isLoading || isLastPage) return
        
        isLoading = true
        
        val currentList = (_sectionInfoList.value ?: emptyList()).toMutableList()
        currentList.add(null)
        _sectionInfoList.value = currentList

        viewModelScope.launch {
            val newSections = getSectionsUseCase(currentPage)
            
            val updatedList = (_sectionInfoList.value ?: emptyList()).toMutableList()
            if (updatedList.isNotEmpty() && updatedList.last() == null) {
                updatedList.removeAt(updatedList.size - 1)
            }

            if (newSections.isEmpty()) {
                isLastPage = true
                _sectionInfoList.value = updatedList
            } else {
                _sectionInfoList.value = updatedList + newSections
                currentPage++
            }

            isLoading = false
        }
    }

    fun loadSectionItems(sectionId: Int) {
        val currentList = _sectionInfoList.value ?: return
        val section = currentList.find { it?.id == sectionId } ?: return

        // products가 null이거나 비어있을 때만 데이터를 불러옵니다.
        if (!section.products.isNullOrEmpty()) return

        viewModelScope.launch {
            val products = getSectionItemsUseCase(sectionId)
            section.products = products

            // 데이터 변경 알림을 위해 LiveData 업데이트
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

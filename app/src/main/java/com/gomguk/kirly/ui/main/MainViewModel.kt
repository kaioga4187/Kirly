package com.gomguk.kirly.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gomguk.kirly.data.SectionInfo
import com.gomguk.kirly.domain.usecase.GetSectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSectionsUseCase: GetSectionsUseCase
) : ViewModel() {

    private val _items = MutableLiveData<List<SectionInfo?>>()
    val items: LiveData<List<SectionInfo?>> = _items

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
            _items.value = newSections
            currentPage++
            _isRefreshing.value = false
        }
    }

    fun loadItems() {
        if (isLoading || isLastPage) return
        
        isLoading = true
        
        // 로딩 시작: 리스트 끝에 null 추가
        val currentList = (_items.value ?: emptyList()).toMutableList()
        currentList.add(null)
        _items.value = currentList

        viewModelScope.launch {
            val newSections = getSectionsUseCase(currentPage)
            
            // 로딩 종료: null 제거
            val updatedList = (_items.value ?: emptyList()).toMutableList()
            if (updatedList.isNotEmpty() && updatedList.last() == null) {
                updatedList.removeAt(updatedList.size - 1)
            }

            if (newSections.isEmpty()) {
                isLastPage = true
                _items.value = updatedList
            } else {
                _items.value = updatedList + newSections
                currentPage++
            }

            isLoading = false
        }
    }
}

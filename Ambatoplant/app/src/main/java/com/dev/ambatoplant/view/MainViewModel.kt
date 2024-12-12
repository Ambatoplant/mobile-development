package com.dev.ambatoplant.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.ambatoplant.data.source.local.entity.HistoryEntity
import com.dev.ambatoplant.domain.repository.AppRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AppRepository
) : ViewModel() {

    fun insertPredictionResult(result: HistoryEntity) {
        viewModelScope.launch {
            repository.insertPredictionResult(result)
        }
    }
}
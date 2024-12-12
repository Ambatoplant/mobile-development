package com.dev.ambatoplant.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dev.ambatoplant.data.source.local.entity.HistoryEntity
import com.dev.ambatoplant.domain.repository.AppRepository
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: AppRepository
) : ViewModel() {

    fun getAllPredictionResult() = repository.getAllPredictionResult().asLiveData()

    fun deletePredictionResult(result: HistoryEntity) {
        viewModelScope.launch {
            repository.deletePredictionResult(result)
        }
    }
}


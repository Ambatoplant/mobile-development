package com.dev.ambatoplant.domain.repository

import com.dev.ambatoplant.data.source.local.entity.CancerEntity
import com.dev.ambatoplant.data.source.local.entity.HistoryEntity
import com.dev.ambatoplant.data.source.remote.network.ApiResponse
import com.dev.ambatoplant.data.source.remote.response.NewsResponse
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    fun getAllPredictionResult(): Flow<List<HistoryEntity>>

    suspend fun insertPredictionResult(result: HistoryEntity)

    suspend fun deletePredictionResult(result: CancerEntity)

    suspend fun getTopHeadlineNews(): Flow<ApiResponse<NewsResponse>>
    suspend fun deletePredictionResult(result: HistoryEntity)
}
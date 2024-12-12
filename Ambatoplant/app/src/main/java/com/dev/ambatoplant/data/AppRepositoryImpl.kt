package com.dev.ambatoplant.data

import android.content.Context
import com.dev.ambatoplant.R
import com.dev.ambatoplant.data.source.local.entity.CancerEntity
import com.dev.ambatoplant.data.source.local.entity.HistoryEntity
import com.dev.ambatoplant.data.source.local.room.HistoryDao
import com.dev.ambatoplant.data.source.remote.network.ApiResponse
import com.dev.ambatoplant.data.source.remote.network.ApiService
import com.dev.ambatoplant.data.source.remote.response.NewsResponse
import com.dev.ambatoplant.domain.repository.AppRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AppRepositoryImpl(
    private val historyDao: HistoryDao,
    private val apiService: ApiService,
    private val context: Context
) : AppRepository {

    override fun getAllPredictionResult(): Flow<List<HistoryEntity>> {
        return historyDao.getAllPredictionResult()
    }

    override suspend fun insertPredictionResult(result: HistoryEntity) {
        historyDao.insertHistoryEntity(result)
    }

    override suspend fun deletePredictionResult(result: CancerEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePredictionResult(result: HistoryEntity) {
        historyDao.deletePredictionResult(result)
    }

    override suspend fun getTopHeadlineNews(): Flow<ApiResponse<NewsResponse>> {
        return flow {
            emit(ApiResponse.Loading)
            try {
                val response = apiService.getTopHeadlineNews()
                val dataArray = response.articles
                if (dataArray.isNullOrEmpty()) {
                    emit(ApiResponse.Empty)
                } else {
                    emit(ApiResponse.Success(response))
                }
            } catch (e: retrofit2.HttpException) {
                val errorMessage = e.response()?.errorBody()?.string()
                val errorResponse = errorMessage?.let {
                    try {
                        Gson().fromJson(it, NewsResponse::class.java)?.message
                    } catch (jsonException: JsonSyntaxException) {
                        null
                    }
                }
                emit(
                    ApiResponse.Error(
                        errorResponse
                            ?: context.getString(R.string.failed_to_display_top_headline_news)
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }
}

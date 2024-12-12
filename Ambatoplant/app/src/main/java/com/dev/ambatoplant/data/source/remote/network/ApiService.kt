package com.dev.ambatoplant.data.source.remote.network

import com.dev.ambatoplant.data.source.remote.response.NewsResponse
import com.dev.ambatoplant.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlineNews(
        @Query("q") query: String = "cancer",
        @Query("category") category: String = "health",
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): NewsResponse
}
package com.dev.ambatoplant.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dev.ambatoplant.data.source.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insertHistoryEntity(historyEntity: HistoryEntity)

    @Query("SELECT * FROM plant")  // Nama tabel yang sesuai dengan entitas
    fun getAllPredictionResult(): Flow<List<HistoryEntity>>

    @Delete
    suspend fun deletePredictionResult(result: HistoryEntity)
}

package com.dev.ambatoplant.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dev.ambatoplant.data.source.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(history: HistoryEntity)

    @Query("SELECT * FROM plant ORDER BY dateTaken DESC")
    suspend fun getAllHistory(): List<HistoryEntity>

}

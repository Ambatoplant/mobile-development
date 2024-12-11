package com.dev.ambatoplant.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "plant")  // Nama tabel dalam database
@Parcelize
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "classificationId")
    val classificationId: Int? = null,  // ID otomatis bertambah

    @ColumnInfo(name = "imageUri")
    val imageUri: String? = null,  // URI gambar

    @ColumnInfo(name = "predictionResult")
    val predictionResult: String? = null,  // Hasil prediksi

    @ColumnInfo(name = "confidenceScore")
    val confidenceScore: Float? = 0F,  // Skor keyakinan, default 0F

    @ColumnInfo(name = "dateTaken")
    val dateTaken: String? = null,  // Tanggal pengambilan gambar

    @ColumnInfo(name = "plantName")  // Kolom untuk menyimpan nama tanaman
    val plantName: String? = null,  // Nama tanaman

    @ColumnInfo(name = "result")  // Kolom untuk menyimpan hasil
    val result: String? = null  // Hasil prediksi atau analisis
) : Parcelable

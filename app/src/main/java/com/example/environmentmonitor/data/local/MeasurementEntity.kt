package com.example.environmentmonitor.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "measurements")
data class MeasurementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val timestamp: Long = System.currentTimeMillis(),

    // gps
    val latitude: Double,
    val longitude: Double,

    // mikrofon
    val noiseLevelDb: Double,

    //aparat (ścieżka do pliku lokalnego)
    val photoPath: String,

    // notatka
    val note: String? = null
)
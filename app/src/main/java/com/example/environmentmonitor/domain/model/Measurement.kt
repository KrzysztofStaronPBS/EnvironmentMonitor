package com.example.environmentmonitor.domain.model

import java.time.LocalDateTime

data class Measurement(
    val id: Int,
    val dateTime: LocalDateTime,
    val latitude: Double,
    val longitude: Double,
    val noiseLevelDb: Double,
    val photoPath: String,
    val note: String?
)
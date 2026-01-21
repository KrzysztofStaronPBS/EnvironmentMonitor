package com.example.environmentmonitor.data.mapper

import com.example.environmentmonitor.data.local.MeasurementEntity
import com.example.environmentmonitor.domain.model.Measurement
import java.time.Instant
import java.time.ZoneId
import java.time.LocalDateTime

fun MeasurementEntity.toDomain(): Measurement {
    return Measurement(
        id = id,
        dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault()
        ),
        latitude = latitude,
        longitude = longitude,
        noiseLevelDb = noiseLevelDb,
        photoPath = photoPath,
        note = note
    )
}

fun Measurement.toEntity(): MeasurementEntity {
    return MeasurementEntity(
        id = id,
        timestamp = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        latitude = latitude,
        longitude = longitude,
        noiseLevelDb = noiseLevelDb,
        photoPath = photoPath,
        note = note
    )
}
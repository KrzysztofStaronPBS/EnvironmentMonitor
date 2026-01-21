package com.example.environmentmonitor.data.repository

import com.example.environmentmonitor.data.local.MeasurementEntity
import com.example.environmentmonitor.domain.model.Measurement
import kotlinx.coroutines.flow.Flow

interface MeasurementRepository {
    // strumień wszystkich pomiarów - UI będzie się automatycznie odświeżać
    fun getAllMeasurements(): Flow<List<Measurement>>

    // pobranie konkretnego pomiaru do ekranu szczegółów
    suspend fun getMeasurementById(id: Int): MeasurementEntity?

    // zapis nowego pomiaru
    suspend fun saveMeasurement(measurement: MeasurementEntity)

    // usunięcie wpisu
    suspend fun deleteMeasurement(measurement: MeasurementEntity)
}
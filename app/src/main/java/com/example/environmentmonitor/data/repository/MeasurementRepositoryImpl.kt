package com.example.environmentmonitor.data.repository

import com.example.environmentmonitor.data.local.MeasurementDao
import com.example.environmentmonitor.data.local.MeasurementEntity
import com.example.environmentmonitor.data.mapper.toDomain
import com.example.environmentmonitor.domain.model.Measurement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeasurementRepositoryImpl @Inject constructor(
    private val dao: MeasurementDao
) : MeasurementRepository {

    override fun getAllMeasurements(): Flow<List<Measurement>> {
        return dao.getAllMeasurements().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getMeasurementById(id: Int): MeasurementEntity? {
        return dao.getMeasurementById(id)
    }

    override suspend fun saveMeasurement(measurement: MeasurementEntity) {
        dao.insertMeasurement(measurement)
    }

    override suspend fun deleteMeasurement(measurement: MeasurementEntity) {
        // czyszczenie systemu plików
        if (measurement.photoPath.isNotEmpty()) {
            try {
                val file = File(measurement.photoPath)
                if (file.exists()) {
                    val deleted = file.delete()
                    if (deleted) {
                        println("FileExporter: Sukces - usunięto plik: ${measurement.photoPath}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // usunięcie rekordu z bazy danych Room
        dao.deleteMeasurement(measurement)
    }
}
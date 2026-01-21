package com.example.environmentmonitor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MeasurementEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val measurementDao: MeasurementDao

    companion object {
        const val DATABASE_NAME = "enviro_db"
    }
}
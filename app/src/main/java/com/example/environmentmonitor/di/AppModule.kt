package com.example.environmentmonitor.di

import android.app.Application
import androidx.room.Room
import com.example.environmentmonitor.data.local.AppDatabase
import com.example.environmentmonitor.data.local.MeasurementDao
import com.example.environmentmonitor.data.sensor.AudioMeter
import com.example.environmentmonitor.data.sensor.LocationClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app,
            AppDatabase::class.java, AppDatabase.DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun provideMeasurementDao(db: AppDatabase): MeasurementDao = db.measurementDao

    @Provides
    @Singleton
    fun provideFusedLocationClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }

    @Provides
    @Singleton
    fun provideLocationClient(app: Application, client: FusedLocationProviderClient): LocationClient {
        return LocationClient(app, client)
    }

    @Provides
    @Singleton
    fun provideAudioMeter(app: Application): AudioMeter {
        return AudioMeter(app)
    }
}
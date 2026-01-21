package com.example.environmentmonitor.di

import com.example.environmentmonitor.data.repository.MeasurementRepository
import com.example.environmentmonitor.data.repository.MeasurementRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMeasurementRepository(
        measurementRepositoryImpl: MeasurementRepositoryImpl
    ): MeasurementRepository
}
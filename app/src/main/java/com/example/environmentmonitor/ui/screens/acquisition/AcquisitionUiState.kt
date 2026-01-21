package com.example.environmentmonitor.ui.screens.acquisition

import android.location.Location

data class AcquisitionUiState(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val noiseLevelDb: Double = 0.0,
    val photoPath: String? = null,
    val isRecording: Boolean = false,
    val hasLocationPermission: Boolean = false,
    val hasAudioPermission: Boolean = false,
    val hasCameraPermission: Boolean = false
)
package com.example.environmentmonitor.ui.screens.acquisition

import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.environmentmonitor.data.local.MeasurementEntity
import com.example.environmentmonitor.data.repository.MeasurementRepository
import com.example.environmentmonitor.data.sensor.AudioMeter
import com.example.environmentmonitor.data.sensor.CameraManager
import com.example.environmentmonitor.data.sensor.LocationClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AcquisitionViewModel @Inject constructor(
    private val repository: MeasurementRepository,
    private val locationClient: LocationClient,
    private val audioMeter: AudioMeter,
    private val cameraManager: CameraManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AcquisitionUiState())
    val uiState = _uiState.asStateFlow()

    private var audioJob: Job? = null

    fun startSensors() {
        // start GPS
        viewModelScope.launch {
            locationClient.getLocationUpdates(2000L).collect { location ->
                _uiState.update { it.copy(
                    latitude = location.latitude,
                    longitude = location.longitude
                ) }
            }
        }

        // start Mikrofon
        audioMeter.start()
        _uiState.update { it.copy(isRecording = true) }

        audioJob = viewModelScope.launch {
            while (true) {
                delay(500) // Odświeżaj dB co 500ms
                _uiState.update { it.copy(noiseLevelDb = audioMeter.getDecibels()) }
            }
        }
    }

    fun stopSensors() {
        audioJob?.cancel()
        audioMeter.stop()
        _uiState.update { it.copy(isRecording = false) }
    }

    fun saveMeasurement(note: String = "") {
        viewModelScope.launch {
            val currentState = _uiState.value
            val entity = MeasurementEntity(
                latitude = currentState.latitude,
                longitude = currentState.longitude,
                noiseLevelDb = currentState.noiseLevelDb,
                photoPath = currentState.photoPath ?: "",
                note = note
            )
            repository.saveMeasurement(entity)
        }
    }

    fun takePhoto(
        controller: LifecycleCameraController,
        onComplete: () -> Unit
    ) {
        cameraManager.takePhoto(controller) { path ->
            _uiState.update { it.copy(photoPath = path) }
            onComplete()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopSensors()
    }
}
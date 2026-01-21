package com.example.environmentmonitor.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.environmentmonitor.data.repository.MeasurementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: MeasurementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadMeasurements()
    }

    private fun loadMeasurements() {
        repository.getAllMeasurements()
            .onStart { _uiState.update { it.copy(isLoading = true) } }
            .onEach { list ->
                _uiState.update { it.copy(measurements = list, isLoading = false) }
            }
            .catch { e ->
                _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun deleteMeasurement(measurement: com.example.environmentmonitor.domain.model.Measurement) {
        viewModelScope.launch {
            // tutaj potrzebny mapper powrotny toEntity()
        }
    }
}
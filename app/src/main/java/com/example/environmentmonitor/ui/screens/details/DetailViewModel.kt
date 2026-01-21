package com.example.environmentmonitor.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.environmentmonitor.data.repository.MeasurementRepository
import com.example.environmentmonitor.domain.model.Measurement
import com.example.environmentmonitor.data.mapper.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MeasurementRepository
) : ViewModel() {

    private val _measurement = MutableStateFlow<Measurement?>(null)
    val measurement = _measurement.asStateFlow()

    fun loadMeasurement(id: Int) {
        viewModelScope.launch {
            val entity = repository.getMeasurementById(id)
            _measurement.value = entity?.toDomain()
        }
    }
}
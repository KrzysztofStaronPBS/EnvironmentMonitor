package com.example.environmentmonitor.ui.screens.dashboard

import com.example.environmentmonitor.domain.model.Measurement

data class DashboardUiState(
    val measurements: List<Measurement> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
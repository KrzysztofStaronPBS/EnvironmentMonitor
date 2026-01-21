package com.example.environmentmonitor.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Acquisition : Screen

    @Serializable
    data object Dashboard : Screen

    @Serializable
    data class Details(val measurementId: Int) : Screen
}
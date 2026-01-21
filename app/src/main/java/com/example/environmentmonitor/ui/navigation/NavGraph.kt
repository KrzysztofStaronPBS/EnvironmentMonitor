package com.example.environmentmonitor.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.environmentmonitor.ui.screens.dashboard.DashboardScreen
import com.example.environmentmonitor.ui.screens.acquisition.AcquisitionScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard
    ) {
        // ekran główny - Lista
        composable<Screen.Dashboard> {
            DashboardScreen(
                onNavigateToAcquisition = {
                    navController.navigate(Screen.Acquisition)
                },
                onNavigateToDetails = { id ->
                    navController.navigate(Screen.Details(measurementId = id))
                }
            )
        }

        // ekran pomiaru (Akwizycja)
        composable<Screen.Acquisition> {
            AcquisitionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ekran szczegółów
        composable<Screen.Details> { backStackEntry ->
            val route: Screen.Details = backStackEntry.toRoute()
            // tu zostanie przekazany route.measurementId do ViewModelu
            Text("Szczegóły pomiaru o ID: ${route.measurementId}")
        }
    }
}
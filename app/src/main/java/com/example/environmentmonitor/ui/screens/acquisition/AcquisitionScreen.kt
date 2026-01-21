package com.example.environmentmonitor.ui.screens.acquisition

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import java.util.Locale
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import android.Manifest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AcquisitionScreen(
    onNavigateBack: () -> Unit,
    viewModel: AcquisitionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // definicja uprawnień
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )
    )

    // automatyczne żądanie uprawnień przy wejściu (tylko raz)
    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    // główny UI
    Scaffold(
        topBar = { TopAppBar(title = { Text("Nowy Pomiar") }) }
    ) { padding ->
        // sprawdzenie stanu uprawnień
        if (permissionsState.allPermissionsGranted) {

            // start sensorów
            DisposableEffect(Unit) {
                viewModel.startSensors()
                onDispose {
                    viewModel.stopSensors()
                }
            }

            // właściwy interfejs pomiarowy
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // karta Hałasu
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Poziom hałasu", style = MaterialTheme.typography.labelLarge)
                        Text(
                            text = "${String.format(Locale.getDefault(), "%.1f", state.noiseLevelDb)} dB",
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }

                // karta Lokalizacji
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Lokalizacja GPS", style = MaterialTheme.typography.labelLarge)
                        Text("Szerokość: ${state.latitude}")
                        Text("Długość: ${state.longitude}")
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        viewModel.saveMeasurement("Pomiar")
                        onNavigateBack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Zapisz pomiar")
                }
            }
        } else {
            // uI dla braku uprawnień
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Wymagane uprawnienia do sensorów.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
                        Text("Przyznaj uprawnienia")
                    }
                }
            }
        }
    }
}
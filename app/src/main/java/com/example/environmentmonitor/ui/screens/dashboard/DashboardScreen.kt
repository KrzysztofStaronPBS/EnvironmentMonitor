package com.example.environmentmonitor.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToAcquisition: () -> Unit,
    onNavigateToDetails: (Int) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Historia Pomiarów") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAcquisition) {
                Icon(Icons.Default.Add, contentDescription = "Nowy pomiar")
            }
        }
    ) { padding ->
        if (state.measurements.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Brak pomiarów. Kliknij +, aby dodać.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.measurements) { measurement ->
                    Card(
                        onClick = { onNavigateToDetails(measurement.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Pomiar #${measurement.id}", style = MaterialTheme.typography.titleMedium)
                            Text(text = "Hałas: ${String.format(Locale.getDefault(), "%.1f", measurement.noiseLevelDb)} dB")
                            Text(text = "Data: ${measurement.dateTime}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
package com.example.environmentmonitor.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.environmentmonitor.util.DateFormatter
import java.util.Locale
import androidx.compose.ui.Alignment.Companion.CenterVertically

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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.measurements) { measurement ->
                    val statusColor = when {
                        measurement.noiseLevelDb < 50.0 -> Color(0xFF4CAF50)
                        measurement.noiseLevelDb < 75.0 -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    }
                    Card(
                        onClick = { onNavigateToDetails(measurement.id) },
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                            verticalAlignment = CenterVertically
                        ) {
                            Box(modifier = Modifier.background(statusColor).fillMaxHeight().width(6.dp))

                            Column(modifier = Modifier.padding(16.dp).weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Pomiar #${measurement.id}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = DateFormatter.formatToDisplay(measurement.dateTime),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Hałas: ${String.format(Locale.getDefault(), "%.1f", measurement.noiseLevelDb)} dB",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = statusColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.example.environmentmonitor.ui.screens.details

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import java.io.File
import java.util.Locale
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    measurementId: Int,
    onNavigateBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val measurement by viewModel.measurement.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(measurementId) {
        viewModel.loadMeasurement(measurementId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły pomiaru #$measurementId") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Wstecz")
                    }
                }
            )
        }
    ) { padding ->
        measurement?.let { m ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // wyświetlanie zapisanego zdjęcia
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    if (m.photoPath.isNotEmpty()) {
                        AsyncImage(
                            model = File(m.photoPath),
                            contentDescription = "Zdjęcie pomiaru",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                            Text("Brak zdjęcia")
                        }
                    }
                }

                // dane tekstowe
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Poziom hałasu: ${String.format(Locale.getDefault(), "%.1f", m.noiseLevelDb)} dB", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Szerokość: ${m.latitude}")
                        Text(text = "Długość: ${m.longitude}")
                        Text(text = "Data: ${m.dateTime}", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Button(
                    onClick = {
                        openMap(
                            context = context,
                            latitude = m.latitude,
                            longitude = m.longitude
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Pokaż lokalizację w Mapach")
                }
            }
        }
    }
}

fun openMap(context: Context, latitude: Double, longitude: Double) {
    val uri = "geo:$latitude,$longitude?q=$latitude,$longitude"
    val intent = Intent(Intent.ACTION_VIEW, uri.toUri()).apply {
        setPackage("com.google.android.apps.maps")
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // fallback jeśli nie ma Google Maps - usuwamy setPackage
        val fallbackIntent = Intent(Intent.ACTION_VIEW, uri.toUri())
        context.startActivity(fallbackIntent)
    }
}
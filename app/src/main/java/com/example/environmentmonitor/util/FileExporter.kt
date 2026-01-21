package com.example.environmentmonitor.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.environmentmonitor.domain.model.Measurement
import java.io.File

class FileExporter(private val context: Context) {

    fun sendMeasurementViaEmail(measurement: Measurement) {
        val uris = ArrayList<Uri>()

        // przygotowanie treści raportu
        val reportContent = """
            RAPORT POMIARU ŚRODOWISKOWEGO #${measurement.id}
            -------------------------------------------
            Data: ${DateFormatter.formatToDisplay(measurement.dateTime)}
            Poziom hałasu: ${measurement.noiseLevelDb} dB
            Lokalizacja: ${measurement.latitude}, ${measurement.longitude}
            Notatka: ${measurement.note ?: "Brak"}
            -------------------------------------------
            Wygenerowano z aplikacji EnvironmentMonitor
        """.trimIndent()

        // zapis do tymczasowego pliku w cache
        val reportFile = File(context.cacheDir, "pomiar_${measurement.id}.txt")
        reportFile.writeText(reportContent)

        // uzyskanie URI przez FileProvider
        val reportUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            reportFile
        )
        uris.add(reportUri)

        // dodawanie zdjęcia do listy załączników
        if (measurement.photoPath.isNotEmpty()) {
            val photoFile = File(measurement.photoPath)
            if (photoFile.exists()) {
                val photoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    photoFile
                )
                uris.add(photoUri)
            }
        }

        // przygotowanie Intentu e-mail
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            // użycie */* ponieważ przesyłamy mieszane typy danych (txt i image)
            type = "*/*"
            putExtra(Intent.EXTRA_SUBJECT, "Raport pomiaru hałasu #${measurement.id}")
            putExtra(Intent.EXTRA_TEXT, "W załączniku przesyłam raport oraz dokumentację fotograficzną.")
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Wyślij kompletny raport..."))
    }
}
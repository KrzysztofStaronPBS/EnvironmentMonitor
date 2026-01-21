package com.example.environmentmonitor.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatter {
    // format czytelny dla użytkownika: Dzień.Miesiąc.Rok Godzina:Minuta
    private const val DATE_PATTERN = "dd.MM.yyyy HH:mm"

    fun formatToDisplay(dateTime: LocalDateTime): String {
        return try {
            val currentFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.getDefault())
            dateTime.format(currentFormatter)
        } catch (e: Exception) {
            dateTime.toString()
        }
    }
}
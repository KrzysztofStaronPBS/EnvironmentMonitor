package com.example.environmentmonitor.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun PermissionDialog(
    permissionText: String,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Wymagane uprawnienie") },
        text = {
            Text(
                text = if (isPermanentlyDeclined) {
                    "Wygląda na to, że na stałe odrzuciłeś uprawnienie do $permissionText. " +
                            "Możesz je włączyć w ustawieniach aplikacji."
                } else {
                    "Aplikacja potrzebuje dostępu do $permissionText, aby poprawnie zbierać dane środowiskowe."
                }
            )
        },
        confirmButton = {
            Button(onClick = {
                if (isPermanentlyDeclined) onGoToAppSettingsClick() else onOkClick()
            }) {
                Text(text = if (isPermanentlyDeclined) "Otwórz ustawienia" else "OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Anuluj")
            }
        }
    )
}
package com.example.practica08.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun CustomSwitch(onCompleteChanged: (Boolean) -> Unit) {
    var isChecked by remember { mutableStateOf(false)}

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("Tarea terminada", style = MaterialTheme.typography.titleMedium)
        Switch(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                onCompleteChanged(it)
            }
        )
    }
}
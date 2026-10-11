package com.example.practica08.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.practica08.data.categoria
import androidx.compose.foundation.lazy.items

fun convertCategory(c: categoria): String {
    return when (c) {
        categoria.Hogar -> "Hogar"
        categoria.Trabajo -> "Trabajo"
        categoria.Escolar -> "Escolar"
        categoria.Social -> "Social"
        categoria.Otro -> "Otro"
        else -> "Error"
    }
}

val listCategories: List<categoria> = listOf(categoria.Otro, categoria.Hogar, categoria.Escolar, categoria.Social, categoria.Trabajo)

@Composable
fun CustomSpinner(onCategoryChange: (categoria) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categoria.Otro) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = convertCategory(selectedCategory),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = true}) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)

                }
            }
        )

        DropdownMenu (
            expanded = expanded,
            onDismissRequest = { expanded = false}
        ) {
            for (c in listCategories) {
                DropdownMenuItem(
                    text = { Text(convertCategory(c))},
                    onClick = {
                        selectedCategory = c
                        onCategoryChange(c)
                        expanded = false
                    }
                )
            }
        }
    }
}
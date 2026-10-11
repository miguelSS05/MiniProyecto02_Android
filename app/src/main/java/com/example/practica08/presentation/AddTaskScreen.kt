package com.example.practica08.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.practica08.components.CustomDatePicker
import com.example.practica08.components.CustomSpinner
import com.example.practica08.components.CustomSwitch
import com.example.practica08.data.Tarea
import com.example.practica08.domain.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.practica08.data.categoria
import kotlin.jvm.java
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color

fun convertCategory(c: categoria): String {
    return when (c) {
        categoria.Hogar -> "Hogar"
        categoria.Trabajo -> "Trabajo"
        categoria.Escolar -> "Escolar"
        categoria.Social -> "Social"
        categoria.Otro -> "Otro"
        else -> {
            "Error"
        }
    }
}

val listCategories: List<categoria> = listOf(categoria.Otro, categoria.Hogar, categoria.Escolar, categoria.Social, categoria.Trabajo)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(onBackClick: () -> Unit, tareaId: Int = -1) {
    val context = LocalContext.current

    val db = remember { AppDatabase.getDatabase(context) }
    val dao = db.usuarioDao()

    val coroutineScope = rememberCoroutineScope()

    val usuarios by dao.obtenerTodos().collectAsState(initial = emptyList())

    var nuevoNombre by remember { mutableStateOf("") }
    var nuevaFecha by remember { mutableStateOf("") }
    var nuevaCategoria by remember { mutableStateOf(categoria.Otro) }
    var nuevoEstatus by remember { mutableStateOf(false) }
    LaunchedEffect(tareaId) {

        if (tareaId != -1) {

            val tarea = dao.obtenerPorId(tareaId)

            tarea?.let {

                nuevoNombre = it.Nombre
                nuevaFecha = it.Fecha
                nuevaCategoria = it.Categoria
                nuevoEstatus = it.Estatus

            }
        }
    }
    var menuExpandido by remember { mutableStateOf(false)}

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("MiniProyecto 02")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Clic en Navegacion", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                actions = {

                    IconButton(onClick = { menuExpandido = true}) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }

                    DropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = { menuExpandido = false}
                    ) {
                        DropdownMenuItem(
                            text = { Text("Configuración") },
                            onClick = {
                                menuExpandido = false
                                Toast.makeText(context, "Configuracion seleccionada", Toast.LENGTH_SHORT).show()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Acerca de") },
                            onClick = {
                                menuExpandido = false
                                Toast.makeText(context, "Acerca de seleccionada", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }

            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 32.dp)
                .padding(horizontal = 12.dp)
        ) {
            Text("Nombre de la tarea", style= MaterialTheme.typography.titleMedium)
            // Nombre tarea
            OutlinedTextField(
                value = nuevoNombre,
                onValueChange = { nuevoNombre = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Tipo de tarea", style= MaterialTheme.typography.titleMedium)
            CustomSpinner(selectedCategory = nuevaCategoria) { c -> nuevaCategoria = c }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Fecha de entrega", style = MaterialTheme.typography.titleMedium)

            if (tareaId != -1) {
                Text(
                    text = "Fecha actual: $nuevaFecha",
                    color = Color.Gray
                )
            }

            CustomDatePicker { d -> nuevaFecha = d }

            Spacer(modifier = Modifier.height(8.dp))

            CustomSwitch(checked = nuevoEstatus) { e -> nuevoEstatus = e }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nuevoNombre.isNotBlank()) {
                        coroutineScope.launch(Dispatchers.IO) {
                            if (tareaId == -1) {
                                dao.insertar(
                                    Tarea(
                                        Nombre = nuevoNombre,
                                        Categoria = nuevaCategoria,
                                        Fecha = nuevaFecha,
                                        Estatus = nuevoEstatus
                                    )
                                )
                            }else{
                                dao.actualizar(
                                    Tarea(
                                        id = tareaId,
                                        Nombre = nuevoNombre,
                                        Categoria = nuevaCategoria,
                                        Fecha = nuevaFecha,
                                        Estatus = nuevoEstatus
                                    )
                                )
                            }
                        }
                        Toast.makeText(context, "Se ha agregado una tarea correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Hubo un error al agregar la tarea", Toast.LENGTH_SHORT).show()
                    }

                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar tarea")
            }

            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}
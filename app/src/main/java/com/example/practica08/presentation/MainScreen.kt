package com.example.practica08.presentation

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.practica08.data.categoria
import com.example.practica08.domain.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current

    val db = remember { AppDatabase.getDatabase(context) }
    val dao = db.usuarioDao()

    val coroutineScope = rememberCoroutineScope()

    val tareas by dao.obtenerTodos().collectAsState(initial = emptyList())

    var menuExpandido by remember { mutableStateOf(false) }
    var mostrarFiltros by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    var filtroEstatus by remember { mutableStateOf<Boolean?>(null) } // null = todas, true = completadas, false = pendientes
    var filtroCategoria by remember { mutableStateOf<categoria?>(null) } // null = todas las categorías

    // Lista filtrada en memoria reactivamente
    val tareasFiltradas = remember(tareas, textoBusqueda, filtroEstatus, filtroCategoria) {
        tareas.filter { tarea ->
            val coincideTexto = tarea.Nombre.contains(textoBusqueda, ignoreCase = true)
            val coincideEstatus = filtroEstatus == null || tarea.Estatus == filtroEstatus
            val coincideCategoria = filtroCategoria == null || tarea.Categoria == filtroCategoria
            coincideTexto && coincideEstatus && coincideCategoria
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MiniProyecto 02") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = { menuExpandido = true }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menú")
                    }

                    DropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = { menuExpandido = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Eliminar todas las tareas") },
                            onClick = {
                                menuExpandido = false
                                coroutineScope.launch(Dispatchers.IO) {
                                    dao.eliminarTodas()
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Se eliminaron todas las tareas",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar tareas completadas") },
                            onClick = {
                                menuExpandido = false
                                coroutineScope.launch(Dispatchers.IO) {
                                    dao.eliminarCompletadas()
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Se eliminaron las tareas completadas",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Marcar todas como completadas") },
                            onClick = {
                                menuExpandido = false
                                coroutineScope.launch(Dispatchers.IO) {
                                    dao.marcarTodasComoCompletadas()
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Todas las tareas marcadas como completadas",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Marcar todas como no completadas") },
                            onClick = {
                                menuExpandido = false
                                coroutineScope.launch(Dispatchers.IO) {
                                    dao.marcarTodasComoPendientes()
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Todas las tareas marcadas como pendientes",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
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
                .padding(top = 16.dp)
                .padding(horizontal = 12.dp)
        ) {
            Button(
                onClick = {
                    val intent = Intent(context, AddTaskActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear nueva tarea")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para alternar la visualización del panel de filtros
            OutlinedButton(
                onClick = { mostrarFiltros = !mostrarFiltros },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = if (mostrarFiltros) Icons.Default.Clear else Icons.Default.FilterList,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (mostrarFiltros) "Ocultar filtros" else "Consultar / filtrar tareas")
            }

            // Panel desplegable de Búsqueda y Filtros
            if (mostrarFiltros) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    label = { Text("Buscar por nombre") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (textoBusqueda.isNotEmpty()) {
                            IconButton(onClick = { textoBusqueda = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Chips para filtrar por estado
                Text("Filtrar por estatus:", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = filtroEstatus == null,
                        onClick = { filtroEstatus = null },
                        label = { Text("Todas") }
                    )
                    FilterChip(
                        selected = filtroEstatus == false,
                        onClick = { filtroEstatus = if (filtroEstatus == false) null else false },
                        label = { Text("Pendientes") }
                    )
                    FilterChip(
                        selected = filtroEstatus == true,
                        onClick = { filtroEstatus = if (filtroEstatus == true) null else true },
                        label = { Text("Completadas") }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Chips horizontales por categoría
                Text("Filtrar por categoría:", style = MaterialTheme.typography.labelMedium)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    items(categoria.values()) { cat ->
                        FilterChip(
                            selected = filtroCategoria == cat,
                            onClick = {
                                filtroCategoria = if (filtroCategoria == cat) null else cat
                            },
                            label = { Text(cat.name) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Lista de Tareas Registradas (${tareasFiltradas.size}):", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            if (tareasFiltradas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (tareas.isEmpty()) "No hay tareas registradas" else "No hay coincidencias con los filtros",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(tareasFiltradas, key = { it.id }) { tarea ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = tarea.Nombre,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text(
                                            text = "[${tarea.Categoria}]",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        if (tarea.Fecha.isNotBlank()) {
                                            Text(
                                                text = "Fecha: ${tarea.Fecha}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val colorEstatus = if (tarea.Estatus) Color(0xFF7DD363) else Color(0xFFCCA48E)
                                    val textoEstatus = if (tarea.Estatus) "Tarea marcada como pendiente" else "Tarea marcada como culminada"
                                    val iconEstatus = if (tarea.Estatus) Icons.Default.Check else Icons.Default.AccessTime

                                    // Botón alternar estatus
                                    IconButton(
                                        onClick = {
                                            coroutineScope.launch(Dispatchers.IO) {
                                                dao.actualizar(tarea.copy(Estatus = !tarea.Estatus))
                                            }
                                            Toast.makeText(context, textoEstatus, Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(40.dp)
                                            .background(colorEstatus)
                                    ) {
                                        Icon(imageVector = iconEstatus, contentDescription = "Estatus", tint = Color.White)
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    // Botón editar
                                    IconButton(
                                        onClick = {
                                            val intent = Intent(context, AddTaskActivity::class.java).apply {
                                                putExtra("ID_TAREA", tarea.id)
                                            }
                                            context.startActivity(intent)
                                        },
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(40.dp)
                                            .background(Color(0xFFEFB584))
                                    ) {
                                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = Color.White)
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    // Botón eliminar individual
                                    IconButton(
                                        onClick = {
                                            coroutineScope.launch(Dispatchers.IO) {
                                                dao.eliminar(tarea)
                                            }
                                            Toast.makeText(context, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(40.dp)
                                            .background(Color(0xFFEC7979))
                                    ) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
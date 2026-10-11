package com.example.practica08.presentation

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.practica08.domain.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendModeColorFilter
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current

    val db = remember { AppDatabase.getDatabase(context) }
    val dao = db.usuarioDao()

    val coroutineScope = rememberCoroutineScope()

    val tareas by dao.obtenerTodos().collectAsState(initial = emptyList())


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
                        menuExpandido = true
                        Toast.makeText(context, "Clic en Navegacion", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menú")
                    }

                    DropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = { menuExpandido = false}
                    ) {
                        DropdownMenuItem(
                            text = { Text("Eliminar todas las tareas") },
                            onClick = {
                                menuExpandido = false
                                Toast.makeText(context, "eliminar todas las tareas seleccionado", Toast.LENGTH_SHORT).show()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar tareas completadas") },
                            onClick = {
                                menuExpandido = false
                                Toast.makeText(context, "eliminar tareas completadas seleccionado", Toast.LENGTH_SHORT).show()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Marcar todas como completadas") },
                            onClick = {
                                menuExpandido = false
                                Toast.makeText(context, "marcar tareas como completadas", Toast.LENGTH_SHORT).show()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Marcar todas como no completadas") },
                            onClick = {
                                menuExpandido = false
                                Toast.makeText(context, "marcar tareas como no completadas", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                actions = {

                    //IconButton(onClick = {}) {
                    //    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Más opciones")
                    //}

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

            Button(
                onClick = {
                    val intent = Intent(context, AddTaskActivity::class.java).apply {}
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear nueva tarea")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Consultar/filtrar tareas")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Lista de Tareas Registradas:",
                style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tareas) { tarea ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text ="${tarea.Nombre}",
                                modifier = Modifier.padding(16.dp)
                            )

                            Row() {
                                val ColorEstatus = if (tarea.Estatus) Color(0xFF7DD363) else Color(
                                    0xFFCCA48E
                                )
                                val TextoEstatus = if (tarea.Estatus) "Tarea marcada como pendiente" else "Tarea marcada como culminada"
                                val IconEstatus = if (tarea.Estatus) Icons.Default.Check else Icons.Default.AccessTime

                                IconButton(
                                    onClick = {
                                        coroutineScope.launch(Dispatchers.IO) {dao.actualizar(tarea.copy(Estatus = !tarea.Estatus)) }
                                        Toast.makeText(context, TextoEstatus, Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .height(42.dp)
                                        .background(ColorEstatus)
                                ) {
                                    Icon(imageVector = IconEstatus, contentDescription = "")
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                IconButton(onClick = {
                                    val intent = Intent(context, AddTaskActivity::class.java).apply {
                                        putExtra(
                                            "ID_TAREA",
                                            tarea.id
                                        )
                                    }
                                    context.startActivity(intent)

                                    Toast.makeText(context, "Editar tarea", Toast.LENGTH_SHORT).show()
                                },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .height(42.dp)
                                        .background(Color(0xFFEFB584))) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Menú")
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                IconButton(onClick = {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        dao.eliminar(tarea)
                                    }
                                    Toast.makeText(context, "Tarea Eliminada", Toast.LENGTH_SHORT).show()
                                },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .height(42.dp)
                                        .background(Color(0xFFEC7979))) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Menú")
                                }

                                Spacer(modifier = Modifier.width(2.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
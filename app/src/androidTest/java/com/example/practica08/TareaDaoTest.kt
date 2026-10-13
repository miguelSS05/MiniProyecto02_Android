package com.example.practica08

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.practica08.data.Tarea
import com.example.practica08.data.TareaDao
import com.example.practica08.data.categoria
import com.example.practica08.domain.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TareaDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: TareaDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Base de datos temporal en memoria
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // Permitir ejecuciones directas para el test
            .build()
        dao = database.usuarioDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertarYObtenerTarea() = runBlocking {
        val nuevaTarea = Tarea(
            Nombre = "Estudiar para el examen",
            Categoria = categoria.Escolar,
            Fecha = "15/10/2026",
            Estatus = false
        )

        dao.insertar(nuevaTarea)

        val listaTareas = dao.obtenerTodos().first()
        assertEquals(1, listaTareas.size)
        assertEquals("Estudiar para el examen", listaTareas[0].Nombre)
    }

    @Test
    fun actualizarEstatusTarea() = runBlocking {
        val tarea = Tarea(id = 1, Nombre = "Lavar ropa", Estatus = false)
        dao.insertar(tarea)

        val tareaGuardada = dao.obtenerTodos().first()[0]
        dao.actualizar(tareaGuardada.copy(Estatus = true))

        val tareaActualizada = dao.obtenerTodos().first()[0]
        assertTrue(tareaActualizada.Estatus)
    }

    @Test
    fun eliminarTarea() = runBlocking {
        val tarea = Tarea(id = 1, Nombre = "Comprar despensa")
        dao.insertar(tarea)

        val tareaGuardada = dao.obtenerTodos().first()[0]
        dao.eliminar(tareaGuardada)

        val lista = dao.obtenerTodos().first()
        assertTrue(lista.isEmpty())
    }
}
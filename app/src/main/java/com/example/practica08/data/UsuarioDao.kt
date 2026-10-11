package com.example.practica08.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {
    @Query("SELECT * FROM tarea")

    fun obtenerTodos(): Flow<List<Tarea>>

    @Insert
    fun insertar(tarea: Tarea)

    @Delete
    suspend fun eliminar(tarea: Tarea)
}
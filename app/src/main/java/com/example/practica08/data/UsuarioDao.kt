package com.example.practica08.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios")

    fun obtenerTodos(): Flow<List<Usuario>>

    @Insert
    fun insertar(usuario: Usuario)

    @Delete
    suspend fun eliminar(usuario: Usuario)
}
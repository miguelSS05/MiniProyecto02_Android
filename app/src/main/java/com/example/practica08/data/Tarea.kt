package com.example.practica08.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tarea")
data class Tarea (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "nombre")
    var Nombre: String= "",

    @ColumnInfo(name = "categoria")
    var Categoria : categoria = categoria.Otro,

    @ColumnInfo(name = "fecha")
    var Fecha: String= "",

    @ColumnInfo(name = "estatus")
    var Estatus : Boolean= false
)


enum class categoria {
    Hogar,
    Trabajo,
    Escolar,
    Social,
    Otro
}
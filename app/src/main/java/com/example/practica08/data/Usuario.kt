package com.example.practica08.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
class Usuario {
    @PrimaryKey(autoGenerate = true)
    var id=0;

    @ColumnInfo(name = "nombre")
    var nombre= "";
    constructor(nombre: String) {
        this.nombre = nombre;
    }
}
package com.example.practica08.domain
import android.content.Context
import androidx.room.*
import com.example.practica08.data.Tarea
import com.example.practica08.data.TareaDao

@Database(entities = [Tarea::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): TareaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "MiniProyectoDos"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
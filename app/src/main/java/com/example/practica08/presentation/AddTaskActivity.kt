package com.example.practica08.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class AddTaskActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idTarea = intent.getIntExtra(
            "ID_TAREA",
            -1
        )

        setContent {
            AddTaskScreen(
                onBackClick = { finish() },
                tareaId = idTarea
            )
        }
    }
}
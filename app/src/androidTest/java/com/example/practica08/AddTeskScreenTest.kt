package com.example.practica08

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.practica08.presentation.AddTaskScreen
import org.junit.Rule
import org.junit.Test

class AddTaskScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // 1. Verificación paso a paso de TODOS los controles del formulario de forma visible y pausada
    @Test
    fun flujoPasoAPasoCompletoFormularioAddTask() {
        var regresoClickeado = false

        composeTestRule.setContent {
            AddTaskScreen(onBackClick = { regresoClickeado = true })
        }

        // PASO 1: Observar pantalla inicial
        Thread.sleep(2000)

        //  PASO 2: Escribir nombre de la tarea
        composeTestRule.onNodeWithText("Nombre de la tarea").assertIsDisplayed()
        composeTestRule.onNodeWithText("")
            .performTextInput("Proyecto Final Android")
        Thread.sleep(2000)

        //  PASO 3: Probar el menú de opciones superior (TopAppBar)
        composeTestRule.onNodeWithContentDescription("Más opciones").performClick()
        Thread.sleep(1500)
        composeTestRule.onNodeWithText("Configuración").assertIsDisplayed()
        composeTestRule.onNodeWithText("Configuración").performClick()
        Thread.sleep(1500)

        // PASO 4: Seleccionar categoría en CustomSpinner (ej. "Trabajo")
        composeTestRule.onNodeWithText("Otro").performClick()
        Thread.sleep(1500)
        composeTestRule.onNodeWithText("Trabajo").performClick()
        Thread.sleep(2000)

        // PASO 5: Interactuar con el DatePicker
        composeTestRule.onNodeWithText("Seleccionar Fecha").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Aceptar").performClick()
        Thread.sleep(2000)

        // PASO 6: Cambiar el Switch de "Tarea terminada"
        composeTestRule.onNodeWithText("Tarea terminada").assertIsDisplayed()
        composeTestRule.onNode(isToggleable()).performClick()
        Thread.sleep(2000)

        //  PASO 7: Presionar "Guardar tarea"
        composeTestRule.onNodeWithText("Guardar tarea").performClick()
        Thread.sleep(2000)

        // Confirmar que finalizó correctamente activando la callback de retorno
        assert(regresoClickeado)
    }

    // 2. Probar validación cuando el nombre está vacío
    @Test
    fun validarQueFormularioVacioNoGuarde() {
        var regresoClickeado = false

        composeTestRule.setContent {
            AddTaskScreen(onBackClick = { regresoClickeado = true })
        }

        Thread.sleep(1500)

        // Intentar guardar con campo vacío
        composeTestRule.onNodeWithText("Guardar tarea").performClick()
        Thread.sleep(2000)

        // Verificar que NO se permitió cerrar/guardar
        assert(!regresoClickeado)
    }
}

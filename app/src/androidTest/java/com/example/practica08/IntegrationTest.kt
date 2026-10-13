package com.example.practica08

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IntegrationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun flujoCompletoCrearTareaYVerificarEnListaConReinicioDeApp() {
        // 1. En MainScreen, presionar el botón "Crear nueva tarea"
        composeTestRule.onNodeWithText("Crear nueva tarea").performClick()

        // 2. En AddTaskScreen, escribir el nombre de la tarea
        composeTestRule.onNodeWithText("")
            .performTextInput("Prueba con Reinicio de App")

        // 3. Presionar "Guardar tarea"
        composeTestRule.onNodeWithText("Guardar tarea").performClick()

        // 4. Verificar que aparece inmediatamente
        composeTestRule.onNodeWithText("Prueba con Reinicio de App").assertIsDisplayed()

        Thread.sleep(1000)

        // 5. CERRAR la app por completo
        composeTestRule.activityRule.scenario.close()

        Thread.sleep(1000)

        // 6. VOLVER A ABRIR MainActivity desde cero en una nueva instancia
        ActivityScenario.launch(MainActivity::class.java).use {
            Thread.sleep(2000)

            // 7. VERIFICAR PERSISTENCIA: La tarea debe seguir apareciendo al abrir la app de nuevo
            composeTestRule.onNodeWithText("Prueba con Reinicio de App").assertIsDisplayed()
        }
    }
}
// Paquete al que pertenece la app
package com.example.picobotella

// Permite acceder al contexto real del dispositivo durante las pruebas
// Ejemplo: como tener acceso al teléfono real mientras se hacen las pruebas
import androidx.test.platform.app.InstrumentationRegistry
// Permite ejecutar pruebas directamente en un dispositivo o emulador Android
import androidx.test.ext.junit.runners.AndroidJUnit4

// Marca una función como una prueba que debe ejecutarse
import org.junit.Test
// Indica qué herramienta se usará para correr las pruebas (AndroidJUnit4)
import org.junit.runner.RunWith

// Importa todas las funciones de verificación (ej: assertEquals, assertTrue)
import org.junit.Assert.*

// Indica que esta clase de pruebas se ejecutará en un dispositivo o emulador real
@RunWith(AndroidJUnit4::class)
// Clase que contiene pruebas automáticas que se corren en el dispositivo
class ExampleInstrumentedTest {

    // Marca esta función como una prueba automática que debe ejecutarse
    @Test
    // Prueba que verifica que el contexto de la app es el correcto
    // Ejemplo: como verificar que el carnet de identidad tiene el nombre correcto
    fun useAppContext() {
        // Obtiene el contexto real de la app que se está probando en el dispositivo
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // Verifica que el nombre del paquete sea exactamente "com.example.picobotella"
        // Si no coincide, la prueba falla y avisa que algo está mal
        assertEquals("com.example.picobotella", appContext.packageName)
    }
}
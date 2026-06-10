// Paquete al que pertenece la app
package com.example.picobotella

// Marca una función como una prueba que debe ejecutarse
import org.junit.Test

// Importa todas las funciones de verificación (ej: assertEquals, assertTrue)
import org.junit.Assert.*

// Clase que contiene pruebas simples que se corren en la computadora
// A diferencia de ExampleInstrumentedTest, estas NO necesitan un dispositivo real
class ExampleUnitTest {

    // Marca esta función como una prueba automática que debe ejecutarse
    @Test
    // Prueba simple que verifica que la suma funciona correctamente
    // Ejemplo: como verificar que una calculadora suma bien antes de usarla
    fun addition_isCorrect() {
        // Verifica que 2 + 2 sea igual a 4
        // Si el resultado fuera diferente, la prueba fallaría y avisaría del error
        assertEquals(4, 2 + 2)
    }
}
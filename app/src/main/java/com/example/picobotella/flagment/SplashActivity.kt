// Paquete al que pertenece la app
package com.example.picobotella

// Permite abrir otras pantallas (ej: pasar del Splash al Home)
import android.content.Intent
// Paquete para guardar el estado de la pantalla
import android.os.Bundle
// Permite ejecutar código después de un tiempo (ej: esperar 5 segundos)
import android.os.Handler
// Hilo principal de la app donde se actualiza la pantalla
import android.os.Looper
// Permite interceptar y controlar el botón de atrás del teléfono
import androidx.activity.OnBackPressedCallback
// Clase base para crear pantallas en Android
import androidx.appcompat.app.AppCompatActivity

// Pantalla de bienvenida que se muestra al abrir la app por 5 segundos
class SplashActivity : AppCompatActivity() {

    // Valores constantes compartidos en toda la clase
    companion object {
        // Duración del splash en milisegundos (5000ms = 5 segundos)
        private const val SPLASH_DURATION = 5000L
    }

    // Se llama cuando Android crea esta pantalla por primera vez
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Carga el diseño visual del splash desde el archivo XML
        setContentView(R.layout.activity_splash)

        // Desactiva el botón de atrás para que el usuario no pueda salir del splash
        bloquearBotonAtras()

        // Programa que después de 5 segundos se ejecute irAMainActivity()
        // Ejemplo: como poner un temporizador de cocina para 5 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            // Cuando pasen los 5 segundos, va al home principal
            irAMainActivity()
        }, SPLASH_DURATION)
    }

    // Bloquea el botón de atrás mientras el splash está visible
    private fun bloquearBotonAtras() {
        // Registra un listener que captura el evento del botón atrás
        // Ejemplo: como poner un guardia en la puerta que no deja salir
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            // Se llama cuando el usuario presiona el botón de atrás
            override fun handleOnBackPressed() {
                // No hace nada, así el botón de atrás queda bloqueado
            }
        })
    }

    // Abre la pantalla principal y cierra el splash
    private fun irAMainActivity() {
        // Crea la orden para abrir MainActivity
        startActivity(Intent(this, MainActivity::class.java))
        // Cierra el splash para que el usuario no pueda volver a él con el botón atrás
        finish()
    }
}
package com.example.picobotella
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
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

        Handler(Looper.getMainLooper()).postDelayed({
            // Cuando pasen los 5 segundos, va al home principal
            irAMainActivity()
        }, SPLASH_DURATION)
    }

    // Bloquea el botón de atrás mientras el splash está visible
    private fun bloquearBotonAtras() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }

    // Abre la pantalla principal y cierra el splash
    private fun irAMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        // Cierra el splash para que el usuario no pueda volver a él con el botón atrás
        finish()
    }
}
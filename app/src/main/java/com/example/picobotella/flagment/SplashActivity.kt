package com.example.picobotella

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_DURATION = 5000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        bloquearBotonAtras()

        Handler(Looper.getMainLooper()).postDelayed({
            irAMainActivity()
        }, SPLASH_DURATION)
    }

    private fun bloquearBotonAtras() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No hacemos nada → botón atrás bloqueado
            }
        })
    }

    private fun irAMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
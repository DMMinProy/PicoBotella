package com.example.picobotella

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var homeFragment: HomeFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    /**
     * Pausa el audio del HomeFragment al navegar a otra pantalla (ej. Instrucciones).
     */
    fun pausarAudioHome() {
        obtenerHomeFragment()?.pausarAudio()
    }

    /**
     * Reanuda el audio del HomeFragment al regresar a la pantalla principal.
     */
    fun reanudarAudioHomeSiCorresponde() {
        obtenerHomeFragment()?.reanudarAudioSiCorresponde()
    }

    /**
     * Busca y retorna el HomeFragment si está activo en el NavHostFragment.
     * Utiliza el tag por defecto generado por el componente de Navigation ("f" + id).
     */
    private fun obtenerHomeFragment(): HomeFragment? {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

        return navHostFragment?.childFragmentManager
            ?.findFragmentByTag("f${R.id.homeFragment}") as? HomeFragment
    }
}
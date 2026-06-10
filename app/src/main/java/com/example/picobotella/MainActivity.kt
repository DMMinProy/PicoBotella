// Paquete al que pertenece la app
package com.example.picobotella

// Paquete para guardar el estado de la pantalla
import android.os.Bundle
// Clase base para crear pantallas en Android
import androidx.appcompat.app.AppCompatActivity
// Permite controlar la navegación entre pantallas
import androidx.navigation.NavController
// Fragmento contenedor que maneja toda la navegación de la app
import androidx.navigation.fragment.NavHostFragment

// Pantalla principal que contiene y coordina todos los fragmentos de la app
class MainActivity : AppCompatActivity() {

    // Controlador que maneja la navegación entre pantallas
    // Ejemplo: como un GPS que sabe en qué pantalla estás y hacia dónde ir
    private lateinit var navController: NavController
    // Referencia al fragmento del home (puede ser null si no está visible)
    private var homeFragment: HomeFragment? = null

    // Se llama cuando Android crea esta pantalla por primera vez
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Carga el diseño visual principal desde el archivo XML
        setContentView(R.layout.activity_main)

        // Busca el contenedor de navegación dentro del layout
        // Ejemplo: como encontrar el marco donde se muestran todas las pantallas
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Obtiene el controlador de navegación del contenedor
        navController = navHostFragment.navController
    }

    // Pausa la música del home, usado cuando se va a otra pantalla
    // Ejemplo: InstruccionesFragment llama esto al abrirse
    fun pausarAudioHome() {
        // Busca el HomeFragment y le pide que pause el audio
        obtenerHomeFragment()?.pausarAudio()
    }

    // Reanuda la música del home si el usuario no la había apagado
    // Ejemplo: se llama al regresar de instrucciones al home
    fun reanudarAudioHomeSiCorresponde() {
        // Busca el HomeFragment y le pide que reanude el audio si corresponde
        obtenerHomeFragment()?.reanudarAudioSiCorresponde()
    }

    // Busca y devuelve el HomeFragment si está activo en pantalla
    // Devuelve null si el home no está visible en este momento
    private fun obtenerHomeFragment(): HomeFragment? {
        // Busca el contenedor de navegación en el fragmentManager
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        // Busca el HomeFragment por su etiqueta única dentro del contenedor
        // La etiqueta "f${R.id.homeFragment}" es generada automáticamente por Navigation
        return navHostFragment?.childFragmentManager
            ?.findFragmentByTag("f${R.id.homeFragment}") as? HomeFragment
    }
}
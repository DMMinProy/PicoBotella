// Paquete al que pertenece la app
package com.example.picobotella

// Paquete para guardar el estado de la pantalla
import android.os.Bundle
// Herramienta para construir la pantalla desde un archivo XML
import android.view.LayoutInflater
// Clase base de cualquier elemento visual en pantalla
import android.view.View
// Contenedor que agrupa varias vistas
import android.view.ViewGroup
// Vista que muestra una imagen (ej: el gif del trofeo)
import android.widget.ImageView
// Barra superior personalizada con título y botón de regreso
import androidx.appcompat.widget.Toolbar
// Clase base para crear pantallas en fragmentos
import androidx.fragment.app.Fragment
// Permite navegar entre pantallas
import androidx.navigation.fragment.findNavController
// Librería para cargar imágenes y GIFs fácilmente
import com.bumptech.glide.Glide

// Pantalla que muestra las instrucciones del juego
class InstruccionesFragment : Fragment() {

    // Se llama cuando Android va a dibujar esta pantalla
    override fun onCreateView(
        // Herramienta para leer el archivo XML del layout
        inflater: LayoutInflater,
        // Contenedor padre donde se pondrá esta pantalla
        container: ViewGroup?,
        // Estado guardado anteriormente si existía
        savedInstanceState: Bundle?
    ): View? {
        // Convierte el XML fragment_instrucciones en una pantalla visible
        return inflater.inflate(R.layout.fragment_instrucciones, container, false)
    }

    // Se llama justo después de que la pantalla ya fue creada y está lista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura el botón de regreso en la barra superior
        configurarToolbar(view)
        // Pausa la música del home al entrar a instrucciones
        pausarAudioHome()

        // Obtiene la vista donde se mostrará el GIF del trofeo
        val ivTriunfo = view.findViewById<ImageView>(R.id.ivTriunfo)
        // Usa Glide para cargar y mostrar el GIF animado del trofeo
        // Ejemplo: como poner un video corto que se repite en una imagen
        Glide.with(this)
            // Indica que el archivo a cargar es un GIF animado
            .asGif()
            // Carga el archivo triunfo.gif desde la carpeta res/raw
            .load(R.raw.triunfo)
            // Lo muestra dentro de la vista ivTriunfo
            .into(ivTriunfo)
    }

    // Configura la barra superior con el botón de flecha para regresar
    private fun configurarToolbar(view: View) {
        // Obtiene la toolbar definida en el XML
        val toolbar = view.findViewById<Toolbar>(R.id.toolbarInstrucciones)
        // Cuando el usuario toca la flecha de regreso
        toolbar.setNavigationOnClickListener {
            // Llama a la función que regresa al home
            volverAlHome()
        }
    }

    // Pausa la música del home al entrar a esta pantalla
    private fun pausarAudioHome() {
        // Accede a MainActivity y llama su función de pausar audio
        // Ejemplo: como pedirle al dueño de la música que la pause
        (activity as? MainActivity)?.pausarAudioHome()
    }

    // Regresa al home y reanuda la música si correspondía
    private fun volverAlHome() {
        // Le dice a MainActivity que reanude la música si estaba encendida
        (activity as? MainActivity)?.reanudarAudioHomeSiCorresponde()
        // Regresa a la pantalla anterior (el home)
        findNavController().navigateUp()
    }

    // Se llama cuando esta pantalla se destruye
    override fun onDestroyView() {
        super.onDestroyView()
        // Reanuda la música del home al salir de instrucciones
        // Cubre el caso en que se salga sin usar el botón de regreso
        (activity as? MainActivity)?.reanudarAudioHomeSiCorresponde()
    }
}
// Paquete al que pertenece la app
package com.example.picobotella

// Permite acceder al contexto de la app (ej: para leer preferencias guardadas)
import android.content.Context
// Paquete para guardar el estado de la pantalla
import android.os.Bundle
// Herramienta para construir la pantalla desde un archivo XML
import android.view.LayoutInflater
// Clase base de cualquier elemento visual en pantalla
import android.view.View
// Contenedor que agrupa varias vistas
import android.view.ViewGroup
// Permite cargar animaciones desde archivos XML (ej: el pop de la estrella)
import android.view.animation.AnimationUtils
// Vista que muestra una imagen (ej: las estrellas)
import android.widget.ImageView
// Vista que muestra texto (ej: el mensaje según las estrellas)
import android.widget.TextView
// Permite obtener colores del archivo colors.xml
import androidx.core.content.ContextCompat
// Controla el comportamiento del panel deslizable (expandido, colapsado)
import com.google.android.material.bottomsheet.BottomSheetBehavior
// Clase base para crear un panel que sube desde abajo de la pantalla
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
// Botón con estilos de Material Design (ej: el botón "Calificar")
import com.google.android.material.button.MaterialButton

// Pantalla de calificación que aparece como panel deslizable desde abajo
class RatingFragment : BottomSheetDialogFragment() {

    // Guarda cuántas estrellas seleccionó el usuario (0 = ninguna)
    private var selectedStars = 0
    // Lista con las 5 imágenes de estrellas
    private lateinit var stars: List<ImageView>
    // Botón para confirmar la calificación
    private lateinit var btnCalificar: MaterialButton
    // Botón para posponer la calificación
    private lateinit var btnAhora: MaterialButton
    // Texto principal que cambia según las estrellas (ej: "¡Increíble!")
    private lateinit var tvMensaje: TextView
    // Texto secundario debajo del mensaje principal
    private lateinit var tvSubtitulo: TextView

    // Mapa con los mensajes para cada cantidad de estrellas
    // Ejemplo: 5 estrellas → "¡Increíble!" / "¡Eres lo máximo!"
    private val mensajes = mapOf(
        1 to Pair("¡Ups!", "Lamentamos tu experiencia.\n¿Qué podemos mejorar?"),
        2 to Pair("Casi...", "Ayúdanos a entender\nqué falló."),
        3 to Pair("¡Bien!", "Está bien, pero podemos\nserlo mucho mejor."),
        4 to Pair("¡Muy bien!", "¡Nos alegra que te\nguste la app!"),
        5 to Pair("¡Increíble!", "¡Eres lo máximo!\nGracias por tu amor.")
    )

    // Valores y funciones compartidas en toda la clase
    companion object {
        // Etiqueta para identificar este fragmento
        const val TAG = "RatingFragment"

        // Función que decide si mostrar o no el panel de calificación
        fun show(context: Context, fragmentManager: androidx.fragment.app.FragmentManager) {
            // Lee las preferencias guardadas de la app
            val prefs = context.getSharedPreferences("picobotella_prefs", Context.MODE_PRIVATE)
            // Si el usuario ya calificó antes, no muestra el panel de nuevo
            if (prefs.getBoolean("ya_califico", false)) return
            // Muestra el panel de calificación
            RatingFragment().show(fragmentManager, TAG)
        }
    }

    // Aplica el tema visual personalizado al panel
    override fun getTheme(): Int = R.style.RatingBottomSheetTheme

    // Se llama cuando Android va a dibujar esta pantalla
    override fun onCreateView(
        // Herramienta para leer el archivo XML del layout
        inflater: LayoutInflater,
        // Contenedor padre donde se pondrá esta pantalla
        container: ViewGroup?,
        // Estado guardado anteriormente si existía
        savedInstanceState: Bundle?
    ): View? {
        // Convierte el XML fragment_rating en una pantalla visible
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    // Se llama justo después de que la pantalla ya fue creada y está lista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Busca el panel deslizable en el diálogo
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        // Si el panel existe, lo configura
        bottomSheet?.let {
            BottomSheetBehavior.from(it).apply {
                // Abre el panel completamente expandido desde el principio
                state = BottomSheetBehavior.STATE_EXPANDED
                // Evita que el panel quede a medias, solo expandido o cerrado
                skipCollapsed = true
            }
        }

        // Obtiene el texto del mensaje principal (ej: "¡Increíble!")
        tvMensaje   = view.findViewById(R.id.tvMensajePrincipal)
        // Obtiene el texto secundario debajo del mensaje
        tvSubtitulo = view.findViewById(R.id.tvSubtitulo)
        // Obtiene el botón de calificar
        btnCalificar = view.findViewById(R.id.btnCalificar)
        // Obtiene el botón de "Ahora no"
        btnAhora     = view.findViewById(R.id.btnMasTarde)

        // Obtiene las 5 imágenes de estrellas del layout
        stars = listOf(
            view.findViewById(R.id.star1),
            view.findViewById(R.id.star2),
            view.findViewById(R.id.star3),
            view.findViewById(R.id.star4),
            view.findViewById(R.id.star5)
        )

        // Activa los clicks en las estrellas
        configurarEstrellas()
        // Activa los clicks en los botones
        configurarBotones()
        // Reproduce la animación de entrada de los elementos
        animarEntrada(view)
    }

    // Pone el listener de click a cada estrella
    private fun configurarEstrellas() {
        // Recorre cada estrella con su posición (0 a 4)
        stars.forEachIndexed { index, star ->
            // Cuando el usuario toca una estrella
            star.setOnClickListener {
                // Guarda cuántas estrellas seleccionó (posición + 1 porque empieza en 0)
                selectedStars = index + 1
                // Actualiza visualmente las estrellas rellenas y vacías
                actualizarEstrellas(selectedStars)
                // Cambia el mensaje según la puntuación
                actualizarMensaje(selectedStars)
                // Habilita el botón calificar ahora que hay una selección
                btnCalificar.isEnabled = true
                // Muestra el botón con opacidad completa (ya no está apagado)
                btnCalificar.alpha = 1f
            }
        }
    }

    // Actualiza el color y estado visual de las estrellas
    private fun actualizarEstrellas(cantidad: Int) {
        // Recorre cada estrella con su posición
        stars.forEachIndexed { index, star ->
            // true si esta estrella debe estar rellena (ej: 3 estrellas → posiciones 0,1,2)
            val filled = index < cantidad
            // Cambia la imagen a estrella rellena o vacía según corresponda
            star.setImageResource(
                if (filled) R.drawable.ic_star_filled else R.drawable.ic_star_empty
            )
            // Elige el color según la puntuación
            // 1-2 estrellas = rojo, 3 = amarillo, 4-5 = verde
            val color = when {
                cantidad <= 2 -> ContextCompat.getColor(requireContext(), R.color.rating_red)
                cantidad == 3 -> ContextCompat.getColor(requireContext(), R.color.rating_yellow)
                else          -> ContextCompat.getColor(requireContext(), R.color.rating_green)
            }
            // Si la estrella está rellena, le aplica el color calculado
            if (filled) star.setColorFilter(color)
            // Si está vacía, quita cualquier color aplicado anteriormente
            else star.clearColorFilter()

            // Solo a la última estrella seleccionada le pone la animación pop
            // Ejemplo: al tocar la 3ra estrella, solo esa rebota
            if (index == cantidad - 1) {
                val pop = AnimationUtils.loadAnimation(requireContext(), R.anim.star_pop)
                star.startAnimation(pop)
            }
        }
    }

    // Cambia el mensaje con una animación suave de desvanecimiento
    private fun actualizarMensaje(cantidad: Int) {
        // Obtiene el par título/subtitulo para esta cantidad de estrellas
        val (titulo, subtitulo) = mensajes[cantidad] ?: return
        // Desvanece el mensaje actual en 150ms
        tvMensaje.animate().alpha(0f).setDuration(150).withEndAction {
            // Cuando terminó de desvanecerse, cambia el texto
            tvMensaje.text   = titulo
            tvSubtitulo.text = subtitulo
            // Aparece el nuevo texto en 200ms
            tvMensaje.animate().alpha(1f).setDuration(200).start()
        }.start()
    }

    // Configura los botones "Calificar" y "Ahora no"
    private fun configurarBotones() {
        // El botón calificar empieza deshabilitado hasta que se toque una estrella
        btnCalificar.isEnabled = false
        // Muestra el botón semitransparente para indicar que está inactivo
        btnCalificar.alpha = 0.5f

        // Cuando el usuario toca "Calificar"
        btnCalificar.setOnClickListener {
            // Guarda la calificación en las preferencias
            guardarCalificacion()
            // Muestra la pantalla de agradecimiento
            mostrarPantallaGracias()
        }

        // Cuando el usuario toca "Ahora no"
        btnAhora.setOnClickListener {
            // Abre las preferencias de la app
            val prefs = requireContext()
                .getSharedPreferences("picobotella_prefs", Context.MODE_PRIVATE)
            // Guarda que no debe mostrar el panel por 3 días
            // Ejemplo: si hoy es lunes, no lo muestra hasta el jueves
            prefs.edit()
                .putLong("posponer_hasta", System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000L)
                .apply()
            // Cierra el panel
            dismiss()
        }
    }

    // Guarda la calificación del usuario en las preferencias
    private fun guardarCalificacion() {
        // Abre las preferencias de la app
        val prefs = requireContext()
            .getSharedPreferences("picobotella_prefs", Context.MODE_PRIVATE)
        // Guarda que ya calificó y cuántas estrellas dio
        prefs.edit()
            .putBoolean("ya_califico", true)
            .putInt("calificacion", selectedStars)
            .apply()

        // Si dio 4 o 5 estrellas, abre la Play Store para calificar oficialmente
        if (selectedStars >= 4) {
            abrirPlayStore()
        }
    }

    // Abre la página de la app en la Play Store
    private fun abrirPlayStore() {
        try {
            // Intenta abrir la app de Play Store directamente
            val intent = android.content.Intent(
                android.content.Intent.ACTION_VIEW,
                // Construye el enlace con el nombre del paquete de la app
                android.net.Uri.parse("market://details?id=${requireContext().packageName}")
            )
            startActivity(intent)
        } catch (e: android.content.ActivityNotFoundException) {
            // Si no tiene Play Store instalada, abre el navegador web
            val intent = android.content.Intent(
                android.content.Intent.ACTION_VIEW,
                // Construye el enlace web de la Play Store
                android.net.Uri.parse(
                    "https://play.google.com/store/apps/details?id=${requireContext().packageName}"
                )
            )
            startActivity(intent)
        }
    }

    // Muestra la pantalla de "Gracias" después de calificar
    private fun mostrarPantallaGracias() {
        // Obtiene el layout de agradecimiento
        val pantallaGracias = view?.findViewById<View>(R.id.layoutGracias)
        // Obtiene el layout de calificación actual
        val pantallaRating  = view?.findViewById<View>(R.id.layoutRating)

        // Desvanece el panel de calificación en 300ms
        pantallaRating?.animate()?.alpha(0f)?.setDuration(300)?.withEndAction {
            // Cuando terminó de desvanecerse, lo oculta completamente
            pantallaRating.visibility = View.GONE
            // Muestra el layout de agradecimiento
            pantallaGracias?.visibility = View.VISIBLE
            // Lo pone transparente para que aparezca con animación
            pantallaGracias?.alpha = 0f
            // Aparece suavemente en 400ms
            pantallaGracias?.animate()?.alpha(1f)?.setDuration(400)?.start()
        }?.start()

        // Cierra el panel automáticamente después de 2 segundos
        view?.postDelayed({ dismiss() }, 2000)
    }

    // Anima la entrada de cada elemento del panel uno por uno
    private fun animarEntrada(root: View) {
        // Lista con todos los elementos que van a animarse al entrar
        val items = listOf<View>(
            root.findViewById(R.id.ivIconoApp),
            root.findViewById(R.id.tvMensajePrincipal),
            root.findViewById(R.id.tvSubtitulo),
            root.findViewById(R.id.layoutEstrellas),
            root.findViewById(R.id.btnCalificar),
            root.findViewById(R.id.btnMasTarde)
        )
        // Recorre cada elemento con su posición
        items.forEachIndexed { i, v ->
            // Lo pone invisible al inicio
            v.alpha = 0f
            // Lo desplaza 30 píxeles hacia abajo antes de aparecer
            v.translationY = 30f
            v.animate()
                // Aparece hasta opacidad completa
                .alpha(1f)
                // Sube a su posición original
                .translationY(0f)
                // Cada elemento espera 80ms más que el anterior
                // Ejemplo: el ícono aparece primero, luego el título, luego las estrellas...
                .setStartDelay((i * 80).toLong())
                // Cada animación dura 350ms
                .setDuration(350)
                .start()
        }
    }
}
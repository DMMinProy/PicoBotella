// Paquete al que pertenece la app, como la dirección de una casa
package com.example.picobotella

// Herramienta para animar propiedades (ej: mover o rotar una vista)
import android.animation.Animator
// Permite escuchar cuando una animación termina (ej: saber cuando la botella paró)
import android.animation.AnimatorListenerAdapter
// Permite animar un objeto cambiando sus propiedades (ej: rotar la botella)
import android.animation.ObjectAnimator
// Permite abrir otras apps o páginas web (ej: abrir el navegador con un enlace)
import android.content.Intent
// Permite crear una dirección web para abrir en el navegador (ej: el enlace de Nequi)
import android.net.Uri
// Permite reproducir audio (ej: la música de fondo)
import android.media.MediaPlayer
// Paquete para guardar el estado de la pantalla
import android.os.Bundle
// Permite crear un contador regresivo (ej: 3, 2, 1, 0)
import android.os.CountDownTimer
// Herramienta para construir la pantalla desde un archivo XML
import android.view.LayoutInflater
// Permite detectar cuando el usuario toca la pantalla
import android.view.MotionEvent
// Clase base de cualquier elemento visual en pantalla
import android.view.View
// Contenedor que agrupa varias vistas (ej: un LinearLayout)
import android.view.ViewGroup
// Hace que la animación vaya frenando al final (ej: la botella desacelera)
import android.view.animation.DecelerateInterpolator
// Botón con imagen (ej: el botón de sonido)
import android.widget.ImageButton
// Vista que muestra una imagen (ej: la botella)
import android.widget.ImageView
// Vista que muestra texto (ej: el contador 3,2,1,0)
import android.widget.TextView
// Clase base para crear pantallas en fragmentos
import androidx.fragment.app.Fragment
// Permite navegar entre pantallas (ej: ir a instrucciones)
import androidx.navigation.fragment.findNavController

// Pantalla principal del juego donde está la botella
class HomeFragment : Fragment() {

    // Reproductor de la música de fondo
    private var mediaPlayer: MediaPlayer? = null
    // Reproductor del sonido cuando gira la botella
    private var mediaPlayerGiro: MediaPlayer? = null
    // Indica si el sonido está activado (true = encendido)
    private var sonidoActivado = true
    // Guarda el ángulo donde quedó la botella (empieza en -25 igual que el XML)
    private var anguloActual = -25f
    // Indica si la botella está girando para evitar doble clic
    private var estaGirando = false

    // Valores constantes compartidos en toda la clase
    companion object {
        // Etiqueta para identificar este fragmento en los logs
        const val TAG = "HomeFragment"

        // Enlace de la app Nequi en la Play Store, usado para simular la calificación
        // Ejemplo: como el enlace que compartes cuando recomiendas una app
        const val URL_NEQUI = "https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es"
    }

    // Se llama cuando Android va a dibujar la pantalla
    override fun onCreateView(
        // Herramienta para leer el archivo XML del layout
        inflater: LayoutInflater,
        // Contenedor padre donde se pondrá esta pantalla
        container: ViewGroup?,
        // Estado guardado anteriormente (si existía)
        savedInstanceState: Bundle?
    ): View? {
        // Convierte el archivo XML homefragment en una pantalla visible
        return inflater.inflate(R.layout.homefragment, container, false)
    }

    // Se llama justo después de que la pantalla ya fue creada y está lista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtiene el botón circular naranja "Presióname"
        val btnGirar        = view.findViewById<View>(R.id.btnGirar)
        // Obtiene la imagen de la botella
        val imgBotella      = view.findViewById<ImageView>(R.id.imgBotella)
        // Obtiene el texto donde se mostrará el contador 3,2,1,0
        val tvNumeroJugador = view.findViewById<TextView>(R.id.tvNumeroJugador)

        // Oculta el contador al inicio porque aún no se ha girado la botella
        tvNumeroJugador.visibility = View.INVISIBLE

        // Crea la animación de parpadeo del botón "Presióname"
        // Ejemplo: como una luz que parpadea encendiéndose y apagándose
        ObjectAnimator.ofFloat(btnGirar, "alpha", 1f, 0f).apply {
            // Cada parpadeo dura medio segundo
            duration = 500
            // Al llegar a invisible, regresa a visible (efecto ping-pong)
            repeatMode = ObjectAnimator.REVERSE
            // Se repite para siempre
            repeatCount = ObjectAnimator.INFINITE
            // Arranca la animación
            start()
        }

        // Carga el archivo de música de fondo desde la carpeta res/raw
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.musica_home)
        // Hace que la música se repita cuando termina
        mediaPlayer?.isLooping = true
        // Comienza a reproducir la música
        mediaPlayer?.start()

        // Obtiene el botón de encender/apagar sonido
        val btnSonido = view.findViewById<ImageButton>(R.id.btnSonido)
        // Detecta cuando el usuario toca el botón de sonido
        btnSonido.setOnClickListener {
            // Cambia el estado: si estaba ON pasa a OFF y viceversa
            sonidoActivado = !sonidoActivado
            // Si el sonido quedó encendido
            if (sonidoActivado) {
                // Muestra el ícono de sonido encendido
                btnSonido.setImageResource(R.drawable.ic_sound)
                // Reanuda la música
                mediaPlayer?.start()
            } else {
                // Muestra el ícono de sonido apagado
                btnSonido.setImageResource(R.drawable.ic_sound_off)
                // Pausa la música
                mediaPlayer?.pause()
            }
        }

        // Obtiene el botón de la estrella (calificar app)
        val btnEstrella = view.findViewById<ImageButton>(R.id.btnEstrella)
        // Cuando el usuario toca la estrella, abre la Play Store para calificar
        // Ejemplo: como cuando tocas "Calificar" en una tienda y te lleva a la reseña
        btnEstrella.setOnClickListener {
            // Crea la dirección web de la app Nequi en la Play Store
            // Ejemplo: como escribir una dirección en Google Maps para ir a ese lugar
            val uri = Uri.parse(URL_NEQUI)
            // Crea la orden para abrir esa dirección web en el navegador o Play Store
            val intent = Intent(Intent.ACTION_VIEW, uri)
            // Abre el navegador o la Play Store con el enlace de Nequi
            startActivity(intent)
        }

        // Obtiene el botón del control de videojuego (instrucciones)
        val btnJuego = view.findViewById<ImageButton>(R.id.btnJuego)
        // Cuando el usuario toca el control, va a la pantalla de instrucciones
        // Ejemplo: como tocar el botón "¿Cómo se juega?" en un juego de mesa
        btnJuego.setOnClickListener {
            // Navega hacia la pantalla de instrucciones usando el navigation graph
            findNavController().navigate(R.id.action_homeFragment_to_instruccionesFragment)
        }

        // Lista con los 5 botones de la barra superior
        listOf(
            view.findViewById<ImageButton>(R.id.btnEstrella),
            view.findViewById<ImageButton>(R.id.btnSonido),
            view.findViewById<ImageButton>(R.id.btnJuego),
            view.findViewById<ImageButton>(R.id.btnAgregar),
            view.findViewById<ImageButton>(R.id.btnCompartir)
            // Recorre cada botón y le pone la animación de press
        ).forEach { boton ->
            // Detecta el toque del usuario sobre cada botón
            boton.setOnTouchListener { v, event ->
                when (event.action) {
                    // Cuando el dedo toca el botón, lo encoge un poco
                    // Ejemplo: como apretar un botón físico que se hunde
                    MotionEvent.ACTION_DOWN ->
                        v.animate().scaleX(0.85f).scaleY(0.85f).setDuration(100).start()
                    // Cuando el dedo se levanta o cancela, vuelve al tamaño normal
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }
                // false = permite que el click normal también funcione
                false
            }
        }

        // Detecta cuando el usuario presiona el botón "Presióname"
        btnGirar.setOnClickListener {
            // Si ya está girando, ignora el toque para evitar doble giro
            if (estaGirando) return@setOnClickListener

            // Marca que la botella está en movimiento
            estaGirando = true
            // Oculta el botón mientras la botella gira
            btnGirar.visibility = View.INVISIBLE
            // Oculta el contador mientras la botella gira
            tvNumeroJugador.visibility = View.INVISIBLE

            // Si el sonido de fondo estaba encendido, lo pausa durante el giro
            if (sonidoActivado) mediaPlayer?.pause()

            // Carga el sonido de la botella girando
            mediaPlayerGiro = MediaPlayer.create(requireContext(), R.raw.sonido_botella)
            // Hace que el sonido de giro se repita mientras la botella gira
            mediaPlayerGiro?.isLooping = true
            // Inicia el sonido de giro
            mediaPlayerGiro?.start()

            // Calcula el ángulo final: mínimo 3 vueltas completas + dirección aleatoria
            // Ejemplo: si quedó en 45°, girará 3-5 vueltas más y parará en un ángulo random
            val anguloFinal = anguloActual + (3..5).random() * 360f + (0..359).random()
            // Duración aleatoria entre 3 y 5 segundos
            val duracion    = (3000..5000).random().toLong()

            // Crea la animación de rotación de la botella
            ObjectAnimator.ofFloat(imgBotella, "rotation", anguloActual, anguloFinal).apply {
                // Aplica la duración aleatoria calculada arriba
                duration = duracion
                // La botella va frenando suavemente al final, como en la vida real
                interpolator = DecelerateInterpolator(2f)
                // Escucha cuando la animación termina
                addListener(object : AnimatorListenerAdapter() {
                    // Se ejecuta justo cuando la botella deja de girar
                    override fun onAnimationEnd(animation: Animator) {
                        // Guarda el ángulo final normalizado entre 0 y 360
                        anguloActual = anguloFinal % 360f
                        // Fija visualmente la botella en ese ángulo final
                        imgBotella.rotation = anguloActual

                        // Detiene el sonido de giro
                        mediaPlayerGiro?.stop()
                        // Libera la memoria usada por el sonido de giro
                        mediaPlayerGiro?.release()
                        // Limpia la referencia para que no quede ocupando memoria
                        mediaPlayerGiro = null

                        // Muestra el contador encima de la botella
                        tvNumeroJugador.visibility = View.VISIBLE
                        // Arranca la cuenta regresiva 3,2,1,0
                        iniciarCuentaRegresiva(tvNumeroJugador, btnGirar)
                    }
                })
                // Arranca la animación de giro
                start()
            }
        }
    }

    // Función que muestra la cuenta regresiva 3,2,1,0 sobre la botella
    private fun iniciarCuentaRegresiva(tvNumeroJugador: TextView, btnGirar: View) {
        // Crea un temporizador de 4 segundos que avisa cada 1 segundo
        object : CountDownTimer(4000L, 1000L) {
            // Se llama cada segundo mientras el contador corre
            override fun onTick(millisUntilFinished: Long) {
                // Muestra el número actual (3, luego 2, luego 1)
                tvNumeroJugador.text = (millisUntilFinished / 1000).toString()
            }

            // Se llama cuando el contador llega a 0
            override fun onFinish() {
                // Muestra el 0 en pantalla
                tvNumeroJugador.text = "0"
                // Espera medio segundo para que el jugador vea el 0
                tvNumeroJugador.postDelayed({
                    // Oculta el contador
                    tvNumeroJugador.visibility = View.INVISIBLE
                    // Indica que la botella ya no está girando
                    estaGirando = false
                    // Muestra el botón "Presióname" de nuevo
                    btnGirar.visibility = View.VISIBLE
                    // Reanuda la música de fondo si estaba encendida
                    if (sonidoActivado) mediaPlayer?.start()
                    // Aquí se lanzará el diálogo con el reto aleatorio (HU 12, pendiente)
                }, 500)
            }
            // Arranca el contador
        }.start()
    }

    // Pausa la música, usado cuando se va a otra pantalla
    fun pausarAudio() { mediaPlayer?.pause() }

    // Reanuda la música solo si el usuario no la apagó manualmente
    fun reanudarAudioSiCorresponde() {
        if (sonidoActivado) mediaPlayer?.start()
    }

    // Se llama cuando el fragmento pasa a segundo plano (ej: minimizar app)
    override fun onPause() {
        super.onPause()
        // Pausa la música de fondo
        mediaPlayer?.pause()
        // Pausa el sonido de giro si estaba sonando
        mediaPlayerGiro?.pause()
    }

    // Se llama cuando el fragmento vuelve a ser visible
    override fun onResume() {
        super.onResume()
        // Reanuda la música solo si el usuario no la había apagado
        if (sonidoActivado) mediaPlayer?.start()
    }

    // Se llama cuando la pantalla se destruye para liberar memoria
    override fun onDestroyView() {
        super.onDestroyView()
        // Detiene, libera y limpia el reproductor de música de fondo
        mediaPlayer?.stop(); mediaPlayer?.release(); mediaPlayer = null
        // Detiene, libera y limpia el reproductor del sonido de giro
        mediaPlayerGiro?.stop(); mediaPlayerGiro?.release(); mediaPlayerGiro = null
    }
}
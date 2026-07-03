package com.example.picobotella

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.picobotella.model.Challenge
import com.example.picobotella.model.PokemonResponse
import com.example.picobotella.viewmodel.ChallengeViewModel

// Pantalla principal del juego
class HomeFragment : Fragment() {

    private var mediaPlayer: MediaPlayer? = null       // Música de fondo
    private var mediaPlayerGiro: MediaPlayer? = null   // Sonido al girar la botella
    private var sonidoActivado = true
    private var anguloActual = -25f  // Ángulo inicial (igual que en el XML)
    private var estaGirando = false
    private val challengeViewModel: ChallengeViewModel by viewModels()
    private var listaDeRetos: List<Challenge> = emptyList()

    companion object {
        const val TAG = "HomeFragment"
        const val URL_NEQUI = "https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.homefragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnGirar        = view.findViewById<View>(R.id.btnGirar)
        val imgBotella      = view.findViewById<ImageView>(R.id.imgBotella)
        val tvNumeroJugador = view.findViewById<TextView>(R.id.tvNumeroJugador)

        tvNumeroJugador.visibility = View.INVISIBLE

        challengeViewModel.allRetos.observe(viewLifecycleOwner) {retos ->
            listaDeRetos = retos.shuffled()
        }

        challengeViewModel.randomPokemon.observe(viewLifecycleOwner) {pokemonData ->
            if (pokemonData != null) {
                mostrarDialogoReto(btnGirar, pokemonData)
                challengeViewModel.clearPokemonState()
            }
        }

        // Animación de parpadeo del botón "Presióname"
        ObjectAnimator.ofFloat(btnGirar, "alpha", 1f, 0f).apply {
            duration = 500
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            start()
        }

        // Iniciar música de fondo en loop
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.musica_home)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        // Botón de sonido: alterna entre encendido y apagado
        val btnSonido = view.findViewById<ImageButton>(R.id.btnSonido)
        btnSonido.setOnClickListener {
            sonidoActivado = !sonidoActivado
            if (sonidoActivado) {
                btnSonido.setImageResource(R.drawable.ic_sound)
                mediaPlayer?.start()
            } else {
                btnSonido.setImageResource(R.drawable.ic_sound_off)
                mediaPlayer?.pause()
            }
        }
        // Navegar a retos
        view.findViewById<ImageButton>(R.id.btnAgregar).setOnClickListener {
            findNavController().navigate(R.id.challengeFragment)
        }

        // Botón estrella: abre la Play Store
        val btnEstrella = view.findViewById<ImageButton>(R.id.btnEstrella)
        btnEstrella.setOnClickListener {
            val uri = Uri.parse(URL_NEQUI)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        // Botón de instrucciones
        val btnJuego = view.findViewById<ImageButton>(R.id.btnJuego)
        btnJuego.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_instruccionesFragment)
        }

        // Boton de compartir
        val btnCompartir = view.findViewById<ImageButton>(R.id.btnCompartir)

        btnCompartir.setOnClickListener {

            val mensaje = """
        App pico botella
        Solo los valientes lo juegan !!
        https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es
    """.trimIndent()

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, mensaje)

            startActivity(Intent.createChooser(intent, "Compartir vía"))
        }
        // Animación de press (escalar) en todos los botones de la barra superior
        listOf(
            view.findViewById<ImageButton>(R.id.btnEstrella),
            view.findViewById<ImageButton>(R.id.btnSonido),
            view.findViewById<ImageButton>(R.id.btnJuego),
            view.findViewById<ImageButton>(R.id.btnAgregar),
            view.findViewById<ImageButton>(R.id.btnCompartir)
        ).forEach { boton ->
            boton.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN ->
                        v.animate().scaleX(0.85f).scaleY(0.85f).setDuration(100).start()
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }
                false
            }
        }

        // Al presionar el botón, gira la botella
        btnGirar.setOnClickListener {
            if (estaGirando) return@setOnClickListener

            estaGirando = true
            btnGirar.visibility = View.INVISIBLE
            tvNumeroJugador.visibility = View.INVISIBLE

            if (sonidoActivado) mediaPlayer?.pause()

            // Reproducir sonido de giro en loop
            mediaPlayerGiro = MediaPlayer.create(requireContext(), R.raw.sonido_botella)
            mediaPlayerGiro?.isLooping = true
            mediaPlayerGiro?.start()

            // Ángulo final: mínimo 3 vueltas completas + posición aleatoria
            val anguloFinal = anguloActual + (3..5).random() * 360f + (0..359).random()
            val duracion    = (4000..5000).random().toLong()



            ObjectAnimator.ofFloat(imgBotella, "rotation", anguloActual, anguloFinal).apply {
                duration = duracion
                interpolator = OvershootInterpolator(1.4f)

                addUpdateListener { animation ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            // El fraction va de 0.0 (inicio) a 1.0 (final)
                            val progreso = animation.animatedFraction
                            val velocidad = 1.0f - (progreso * 0.7f) // Baja de 100% de velocidad a un 30%

                            mediaPlayerGiro?.let { mp ->
                                if (mp.isPlaying) {
                                    mp.playbackParams = mp.playbackParams.setSpeed(velocidad)
                                }
                            }
                        } catch (e: Exception) {
                            // Evita caídas si el MediaPlayer se libera en paralelo
                        }
                    }
                }

                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        anguloActual = anguloFinal

                        mediaPlayerGiro?.stop()
                        mediaPlayerGiro?.release()
                        mediaPlayerGiro = null

                        tvNumeroJugador.visibility = View.VISIBLE
                        iniciarCuentaRegresiva(tvNumeroJugador, btnGirar)
                    }
                })
                start()
            }
        }
    }

    // Cuenta regresiva 3,2,1,0 tras detener la botella
    private fun iniciarCuentaRegresiva(tvNumeroJugador: TextView, btnGirar: View) {
        object : CountDownTimer(4000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                tvNumeroJugador.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                tvNumeroJugador.text = "0"
                tvNumeroJugador.postDelayed({
                    tvNumeroJugador.visibility = View.INVISIBLE

                    // Dispara la petición de red asíncrona al ViewModel.
                    // Los observadores en onViewCreated se encargarán de abrir el diálogo al recibir la respuesta.
                    challengeViewModel.fetchRandomPokemon()
                }, 500)
            }
        }.start()
    }

    /**
     * Selecciona un reto aleatorio de la lista y lo despliega en un AlertDialog.
     */
    private fun mostrarDialogoReto(btnGirar: View, pokemon: PokemonResponse) {
        val challengeText = if (listaDeRetos.isNotEmpty()) {
            listaDeRetos.random().description
        } else {
            "¡No hay retos guardados! Agrega algunos desde el menú."
        }

        val pokemonForm = pokemon.forms.firstOrNull()
        val pokemonName = pokemonForm?.name?.replaceFirstChar { it.uppercase() } ?: "Desconocido"
        val pokemonId = pokemonForm?.url?.split("/")?.dropLast(1)?.last() ?: "1"
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_challenge, null)

        // Vincular elementos de la vista personalizada
        val tvChallengeDescription = dialogView.findViewById<TextView>(R.id.tvChallengeDescription)
        val btnDismissChallenge = dialogView.findViewById<View>(R.id.btnDismissChallenge)
        val imgPokemon = dialogView.findViewById<ImageView>(R.id.imgPokemon) // El ImageView de tu círculo

        tvChallengeDescription.text = "Pokémon: $pokemonName\n\n$challengeText"

        imgPokemon.load(imageUrl) {
            crossfade(true) // Hace una transición suave tipo "fade-in" cuando termine de descargar
            placeholder(R.drawable.ic_launcher_background) // Imagen temporal mientras descarga de red
            error(R.drawable.ic_launcher_background) // Imagen de respaldo por si falla la descarga
        }

        // Construir el AlertDialog con la vista personalizada
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false) // No se puede cerrar al hacer click afuera
            .create()


        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        btnDismissChallenge.setOnClickListener {
            dialog.dismiss()
            restablecerEstadoJuego(btnGirar)
        }

        dialog.show()
    }

    /**
     * Devuelve la interfaz a su estado inicial para permitir un nuevo giro.
     */
    private fun restablecerEstadoJuego(btnGirar: View) {
        estaGirando = false
        btnGirar.visibility = View.VISIBLE
        if (sonidoActivado) mediaPlayer?.pause()
    }

    fun pausarAudio() { mediaPlayer?.pause() }

    fun reanudarAudioSiCorresponde() {
        if (sonidoActivado) mediaPlayer?.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
        mediaPlayerGiro?.pause()
    }

    override fun onResume() {
        super.onResume()
        if (sonidoActivado) mediaPlayer?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.stop(); mediaPlayer?.release(); mediaPlayer = null
        mediaPlayerGiro?.stop(); mediaPlayerGiro?.release(); mediaPlayerGiro = null
    }
}
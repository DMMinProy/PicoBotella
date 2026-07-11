package com.example.picobotella

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.picobotella.databinding.DialogChallengeBinding
import com.example.picobotella.databinding.HomefragmentBinding
import com.example.picobotella.model.Challenge
import com.example.picobotella.model.PokemonResponse
import com.example.picobotella.viewmodel.ChallengeViewModel

class HomeFragment : Fragment() {

    private var _binding: HomefragmentBinding? = null
    private val binding get() = _binding!!

    private var mediaPlayer: MediaPlayer? = null
    private var mediaPlayerGiro: MediaPlayer? = null
    private var sonidoActivado = true

    private var anguloActual = -25f
    private var estaGirando = false
    private var cuentaRegresiva: CountDownTimer? = null

    private val challengeViewModel: ChallengeViewModel by viewModels()
    private var listaDeRetos: List<Challenge> = emptyList()

    companion object {
        const val TAG = "HomeFragment"
        const val URL_PLAY_STORE = "https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomefragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvNumeroJugador.visibility = View.INVISIBLE
        configurarObservadores()
        configurarMusica()
        configurarAnimacionParpadeo()
        configurarBotones()
    }
    private fun configurarObservadores() {
        challengeViewModel.allRetos.observe(viewLifecycleOwner) { retos ->
            listaDeRetos = retos.shuffled()
        }

        challengeViewModel.randomPokemon.observe(viewLifecycleOwner) { pokemonData ->
            if (pokemonData != null) {
                mostrarDialogoReto(pokemonData)
                challengeViewModel.clearPokemonState()
            }
        }
    }

    // ── Música ─────────────────────────────────────────────────────────────

    private fun configurarMusica() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.musica_home)?.apply {
            isLooping = true
            start()
        }
    }

    // ── Animación parpadeo ─────────────────────────────────────────────────

    private fun configurarAnimacionParpadeo() {
        ObjectAnimator.ofFloat(binding.btnGirar, "alpha", 1f, 0f).apply {
            duration = 500
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            start()
        }
    }

    // ── Botones ────────────────────────────────────────────────────────────

    private fun configurarBotones() {
        // Sonido
        binding.btnSonido.setOnClickListener {
            sonidoActivado = !sonidoActivado
            if (sonidoActivado) {
                binding.btnSonido.setImageResource(R.drawable.ic_sound)
                mediaPlayer?.start()
            } else {
                binding.btnSonido.setImageResource(R.drawable.ic_sound_off)
                mediaPlayer?.pause()
            }
        }

        // Estrella → Play Store
        binding.btnEstrella.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAY_STORE))
            startActivity(intent)
        }

        // Instrucciones
        binding.btnJuego.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_instruccionesFragment)
        }

        // Retos
        binding.btnAgregar.setOnClickListener {
            findNavController().navigate(R.id.challengeFragment)
        }

        // Compartir
        binding.btnCompartir.setOnClickListener {
            val mensaje = """
                App Pico Botella
                ¡Solo los valientes lo juegan!
                $URL_PLAY_STORE
            """.trimIndent()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, mensaje)
            }
            startActivity(Intent.createChooser(intent, "Compartir vía"))
        }

        // Animación press en botones de la barra
        listOf(
            binding.btnEstrella,
            binding.btnSonido,
            binding.btnJuego,
            binding.btnAgregar,
            binding.btnCompartir
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

        // Girar botella
        binding.btnGirar.setOnClickListener {
            if (estaGirando) return@setOnClickListener
            girarBotella()
        }
    }

    // ── Lógica de giro ─────────────────────────────────────────────────────

    private fun girarBotella() {
        estaGirando = true
        binding.btnGirar.visibility = View.INVISIBLE
        binding.tvNumeroJugador.visibility = View.INVISIBLE

        if (sonidoActivado) mediaPlayer?.pause()

        mediaPlayerGiro?.release()
        mediaPlayerGiro = MediaPlayer.create(requireContext(), R.raw.sonido_botella)?.apply {
            isLooping = true
            start()
        }

        val anguloFinal = anguloActual + (3..5).random() * 360f + (0..359).random()
        val duracion = (4000..5000).random().toLong()

        ObjectAnimator.ofFloat(binding.imgBotella, "rotation", anguloActual, anguloFinal).apply {
            duration = duracion
            interpolator = OvershootInterpolator(1.4f)

            addUpdateListener { animation ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        val progreso = animation.animatedFraction
                        val velocidad = (1.0f - progreso * 0.7f).coerceAtLeast(0.3f)
                        mediaPlayerGiro?.takeIf { it.isPlaying }?.let {
                            it.playbackParams = it.playbackParams.setSpeed(velocidad)
                        }
                    } catch (e: Exception) { }
                }
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    anguloActual = anguloFinal % 360f
                    liberarSonidoGiro()
                    binding.tvNumeroJugador.visibility = View.VISIBLE
                    iniciarCuentaRegresiva()
                }
            })
            start()
        }
    }

    // ── Cuenta regresiva ───────────────────────────────────────────────────

    private fun iniciarCuentaRegresiva() {
        cuentaRegresiva?.cancel()
        cuentaRegresiva = object : CountDownTimer(4000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvNumeroJugador.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                binding.tvNumeroJugador.text = "0"
                binding.tvNumeroJugador.postDelayed({
                    if (isAdded) {
                        binding.tvNumeroJugador.visibility = View.INVISIBLE
                        challengeViewModel.fetchRandomPokemon()
                    }
                }, 500)
            }
        }.start()
    }

    // ── Diálogo de reto ────────────────────────────────────────────────────

    private fun mostrarDialogoReto(pokemon: PokemonResponse) {
        if (!isAdded) return

        val challengeText = if (listaDeRetos.isNotEmpty()) {
            listaDeRetos.random().description
        } else {
            "¡No hay retos guardados! Agrega algunos desde el menú."
        }

        val pokemonName = pokemon.name.replaceFirstChar { it.uppercase() }
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
        val dialogBinding = DialogChallengeBinding.inflate(LayoutInflater.from(requireContext()))

        dialogBinding.tvChallengeDescription.text = "Pokémon: $pokemonName\n\n$challengeText"

        dialogBinding.imgPokemon.load(imageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnDismissChallenge.setOnClickListener {
            dialog.dismiss()
            restablecerEstadoJuego()
        }

        dialog.show()
    }

    // ── Estado del juego ───────────────────────────────────────────────────

    private fun restablecerEstadoJuego() {
        estaGirando = false
        binding.btnGirar.visibility = View.VISIBLE
        if (sonidoActivado) mediaPlayer?.start()
    }

    private fun liberarSonidoGiro() {
        mediaPlayerGiro?.stop()
        mediaPlayerGiro?.release()
        mediaPlayerGiro = null
    }

    // ── Audio público ──────────────────────────────────────────────────────

    fun pausarAudio() { mediaPlayer?.pause() }

    fun reanudarAudioSiCorresponde() {
        if (sonidoActivado) mediaPlayer?.start()
    }

    // ── Ciclo de vida ──────────────────────────────────────────────────────

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
        cuentaRegresiva?.cancel()
        cuentaRegresiva = null
        mediaPlayer?.stop(); mediaPlayer?.release(); mediaPlayer = null
        mediaPlayerGiro?.stop(); mediaPlayerGiro?.release(); mediaPlayerGiro = null
        _binding = null  // ← siempre al final
    }
}
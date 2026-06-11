package com.example.picobotella

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

// Pantalla principal del juego
class HomeFragment : Fragment() {

    private var mediaPlayer: MediaPlayer? = null       // Música de fondo
    private var mediaPlayerGiro: MediaPlayer? = null   // Sonido al girar la botella
    private var sonidoActivado = true
    private var anguloActual = -25f  // Ángulo inicial (igual que en el XML)
    private var estaGirando = false

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
            val duracion    = (3000..5000).random().toLong()

            ObjectAnimator.ofFloat(imgBotella, "rotation", anguloActual, anguloFinal).apply {
                duration = duracion
                interpolator = DecelerateInterpolator(2f)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        anguloActual = anguloFinal % 360f
                        imgBotella.rotation = anguloActual

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
                    estaGirando = false
                    btnGirar.visibility = View.VISIBLE
                    if (sonidoActivado) mediaPlayer?.start()
                    // TODO HU-12: lanzar diálogo con reto aleatorio
                }, 500)
            }
        }.start()
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
package com.example.picobotella

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.MotionEvent
import android.widget.ImageButton

class HomeFragment : Fragment() {

    private var mediaPlayer: MediaPlayer? = null
    private var sonidoActivado = true  // ← estado inicial del sonido

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.homefragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animación parpadeo
        val btnGirar = view.findViewById<View>(R.id.btnGirar)
        ObjectAnimator.ofFloat(btnGirar, "alpha", 1f, 0f).apply {
            duration = 500
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            start()
        }

        // Iniciar música
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.musica_home)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        // Botón sonido
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

        // Animación press en botones de navegación
        val botones = listOf(
            view.findViewById<ImageButton>(R.id.btnEstrella),
            view.findViewById<ImageButton>(R.id.btnSonido),
            view.findViewById<ImageButton>(R.id.btnJuego),
            view.findViewById<ImageButton>(R.id.btnAgregar),
            view.findViewById<ImageButton>(R.id.btnCompartir)
        )

        botones.forEach { boton ->
            boton.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.animate()
                            .scaleX(0.85f)
                            .scaleY(0.85f)
                            .setDuration(100)
                            .start()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                    }
                }
                false
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        // Solo reanuda si el sonido está activado
        if (sonidoActivado) {
            mediaPlayer?.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
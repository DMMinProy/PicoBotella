package com.example.picobotella

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

// Pantalla de instrucciones del juego
class InstruccionesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_instrucciones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarToolbar(view)
        pausarAudioHome()

        // Cargar GIF del trofeo con Glide
        val ivTriunfo = view.findViewById<ImageView>(R.id.ivTriunfo)
        Glide.with(this)
            .asGif()
            .load(R.raw.triunfo)
            .into(ivTriunfo)
    }

    private fun configurarToolbar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbarInstrucciones)
        toolbar.setNavigationOnClickListener { volverAlHome() }
    }

    private fun pausarAudioHome() {
        (activity as? MainActivity)?.pausarAudioHome()
    }

    // Reanuda la música y regresa al home
    private fun volverAlHome() {
        (activity as? MainActivity)?.reanudarAudioHomeSiCorresponde()
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Cubre el caso de salida sin usar el botón de regreso
        (activity as? MainActivity)?.reanudarAudioHomeSiCorresponde()
    }
}
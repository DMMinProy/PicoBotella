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
import com.example.picobotella.databinding.FragmentInstruccionesBinding
import com.example.picobotella.view.MainActivity

// Pantalla de instrucciones del juego
class InstruccionesFragment : Fragment() {
    private var _binding: FragmentInstruccionesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstruccionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarToolbar(view)
        pausarAudioHome()

        // Cargar GIF del trofeo con Glide
        Glide.with(this)
            .asGif()
            .load(R.raw.triunfo)
            .into(binding.ivTriunfo)
    }

    private fun configurarToolbar(view: View) {
        binding.toolbarInstrucciones.setNavigationOnClickListener { volverAlHome() }
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
        _binding = null
        // Cubre el caso de salida sin usar el botón de regreso
        (activity as? MainActivity)?.reanudarAudioHomeSiCorresponde()
    }
}
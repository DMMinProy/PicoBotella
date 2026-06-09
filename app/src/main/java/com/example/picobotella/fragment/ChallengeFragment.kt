package com.example.picobotella

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.picobotella.viewmodel.ChallengeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChallengeFragment : Fragment() {
    //private lateinit var viewModel: ChallengeViewModel
    private lateinit var adapter: ChallengeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ── RecyclerView ───
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_challenges)
        val tvVacia = view.findViewById<TextView>(R.id.tv_lista_vacia)

        adapter = ChallengeAdapter(
            challenges = emptyList(),
            onEditClick = {
                // COMMIT 2: aquí irá el diálogo de editar
                Toast.makeText(requireContext(), "Editar: ${it.description}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = {
                // HU 9: el compañero lo implementa
                Toast.makeText(requireContext(), "Eliminar: ${it.description}", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // ── ViewModel ─────────────────────────────────────────
        //viewModel = ViewModelProvider(this)[ChallengeViewModel::class.java]

        // Observar lista: cuando cambia la BD, se actualiza la pantalla sola
        //viewModel.allRetos.observe(viewLifecycleOwner) { retos ->
            //adapter.updateList(retos)

            // Mostrar mensaje si no hay retos
            //if (retos.isEmpty()) {
                //tvVacia.visibility = View.VISIBLE
                //recyclerView.visibility = View.GONE
            //} else {
                //tvVacia.visibility = View.GONE
               // recyclerView.visibility = View.VISIBLE
           // }
        //}
        val retosPrueba = listOf(
            com.example.picobotella.database.Challenge(
                description = "Tomar un vaso de agua"
            ),
            com.example.picobotella.database.Challenge(
                description = "Cantar una canción"
            ),
            com.example.picobotella.database.Challenge(
                description = "Bailar 10 segundos"
            )
        )

        adapter.updateList(retosPrueba)

        tvVacia.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        // ── FAB (por ahora solo un Toast, mañana abre diálogo) ──
        view.findViewById<FloatingActionButton>(R.id.fab_agregar_challenge).setOnClickListener {
            Toast.makeText(requireContext(), "¡Mañana aquí va el diálogo!", Toast.LENGTH_SHORT).show()
        }

        // ── Botón atrás ────────────────────────────────────────
        view.findViewById<View>(R.id.btn_back_challenge).setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
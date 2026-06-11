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
import com.example.picobotella.fragment.AddChallengeDialogFragment
import com.example.picobotella.viewmodel.ChallengeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChallengeFragment : Fragment() {
    private lateinit var viewModel: ChallengeViewModel
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
            onEditClick = { challenge ->

                EditChallengeDialogFragment(
                    currentDescription = challenge.description
                ) { newDescription ->

                    // TODO SQLITE
                    // viewModel.update(
                    //    challenge.copy(description = newDescription)
                    // )

                    Toast.makeText(
                        requireContext(),
                        "Reto actualizado: $newDescription",
                        Toast.LENGTH_SHORT
                    ).show()

                }.show(parentFragmentManager, "EDIT_CHALLENGE")
            },
            onDeleteClick = {
                // HU 9: el compañero lo implementa
                Toast.makeText(requireContext(), "Eliminar: ${it.description}", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

         //── ViewModel ──
        viewModel = ViewModelProvider(this)[ChallengeViewModel::class.java]

        // Observar lista: cuando cambia la BD, se actualiza la pantalla sola
        viewModel.allRetos.observe(viewLifecycleOwner) { retos ->
            adapter.updateList(retos)

            // Mostrar mensaje si no hay retos
            if (retos.isEmpty()) {
                tvVacia.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                tvVacia.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        // ── FAB (por ahora solo un Toast, mañana abre diálogo) ──
        view.findViewById<FloatingActionButton>(R.id.fab_agregar_challenge)
            .setOnClickListener {

                AddChallengeDialogFragment { texto ->

                    viewModel.insert(texto)

                    Toast.makeText(
                        requireContext(),
                        "Reto guardado: $texto",
                        Toast.LENGTH_SHORT
                    ).show()

                }.show(parentFragmentManager, "ADD_CHALLENGE")
            }

        // ── Botón atrás ────────────────────────────────────────
        view.findViewById<View>(R.id.btn_back_challenge).setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
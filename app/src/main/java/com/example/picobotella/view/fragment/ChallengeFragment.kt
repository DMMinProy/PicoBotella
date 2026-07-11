package com.example.picobotella

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.picobotella.databinding.FragmentChallengeBinding
import com.example.picobotella.view.adapters.ChallengeAdapter
import com.example.picobotella.view.fragment.AddChallengeDialogFragment
import com.example.picobotella.view.fragment.DeleteChallengeDialogFragment
import com.example.picobotella.viewmodel.ChallengeViewModel

class ChallengeFragment : Fragment() {

    // ── ViewBinding ────────────────────────────────────────────────────────
    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!

    // ── ViewModel ──────────────────────────────────────────────────────────
    private val viewModel: ChallengeViewModel by viewModels()  // ← forma moderna, sin ViewModelProvider

    // ── Adapter ───────────────────────────────────────────────────────────
    private lateinit var adapter: ChallengeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarAdapter()
        configurarRecyclerView()
        configurarObservadores()
        configurarBotones()
    }

    // ── Adapter ────────────────────────────────────────────────────────────

    private fun configurarAdapter() {
        adapter = ChallengeAdapter(
            challenges = emptyList(),
            onEditClick = { challenge ->
                EditChallengeDialogFragment(challenge) { updatedChallenge ->
                    viewModel.update(updatedChallenge)
                    Toast.makeText(
                        requireContext(),
                        "Reto actualizado: ${updatedChallenge.description}",
                        Toast.LENGTH_SHORT
                    ).show()
                }.show(parentFragmentManager, "EDIT_CHALLENGE")
            },
            onDeleteClick = { challenge ->
                DeleteChallengeDialogFragment(challenge) { deletedChallenge ->
                    viewModel.delete(deletedChallenge)
                    Toast.makeText(
                        requireContext(),
                        "Reto eliminado",
                        Toast.LENGTH_SHORT
                    ).show()
                }.show(parentFragmentManager, "DELETE_CHALLENGE")
            }
        )
    }

    // ── RecyclerView ───────────────────────────────────────────────────────

    private fun configurarRecyclerView() {
        binding.recyclerChallenges.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerChallenges.adapter = adapter
    }

    // ── Observadores ───────────────────────────────────────────────────────

    private fun configurarObservadores() {
        viewModel.allRetos.observe(viewLifecycleOwner) { retos ->
            adapter.updateList(retos)

            val listaVacia = retos.isEmpty()
            binding.tvListaVacia.visibility = if (listaVacia) View.VISIBLE else View.GONE
            binding.recyclerChallenges.visibility = if (listaVacia) View.GONE else View.VISIBLE
        }
    }

    // ── Botones ────────────────────────────────────────────────────────────

    private fun configurarBotones() {
        binding.fabAgregarChallenge.setOnClickListener {
            AddChallengeDialogFragment { texto ->
                viewModel.insert(texto)
                Toast.makeText(
                    requireContext(),
                    "Reto guardado: $texto",
                    Toast.LENGTH_SHORT
                ).show()
            }.show(parentFragmentManager, "ADD_CHALLENGE")
        }

        binding.btnBackChallenge.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // ── Ciclo de vida ──────────────────────────────────────────────────────

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
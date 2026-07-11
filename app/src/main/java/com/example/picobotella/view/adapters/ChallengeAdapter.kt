package com.example.picobotella.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.picobotella.R
import com.example.picobotella.databinding.ItemChallengeBinding
import com.example.picobotella.model.Challenge

// Adapter = le dice al RecyclerView cómo dibujar cada reto en pantalla
class ChallengeAdapter(
    private var challenges: List<Challenge>,
    private val onEditClick: (Challenge) -> Unit,
    private val onDeleteClick: (Challenge) -> Unit
) : RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    // ViewHolder = representa visualmente UNA fila de la lista
    inner class ChallengeViewHolder(private val binding: ItemChallengeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(challenge: Challenge) {
            binding.tvDescripcionChallenge.text = challenge.description

            binding.btnEditChallenge.setOnClickListener {
                animarBoton(it) {onEditClick(challenge)}
            }

            binding.btnDeleteChallenge.setOnClickListener {
                animarBoton(it) {onDeleteClick(challenge)}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val binding = ItemChallengeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ChallengeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.bind(challenge)
    }

    override fun getItemCount() = challenges.size

    // Llama esto cuando LiveData entrega una lista nueva
    fun updateList(newList: List<Challenge>) {
        challenges = newList
        notifyDataSetChanged()
    }

    // Animación: el botón se encoge y regresa antes de ejecutar la acción
    private fun animarBoton(view: View, accion: () -> Unit) {
        view.animate()
            .scaleX(0.80f).scaleY(0.80f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f).scaleY(1f)
                    .setDuration(100)
                    .withEndAction { accion() }
                    .start()
            }.start()
    }
}
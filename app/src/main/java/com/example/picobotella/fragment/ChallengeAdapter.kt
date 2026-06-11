package com.example.picobotella
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.picobotella.R
import com.example.picobotella.database.Challenge

// Adapter = le dice al RecyclerView cómo dibujar cada reto en pantalla
class ChallengeAdapter(
    private var challenges: List<Challenge>,
    private val onEditClick: (Challenge) -> Unit,
    private val onDeleteClick: (Challenge) -> Unit
) : RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    // ViewHolder = representa visualmente UNA fila de la lista
    inner class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDescripcion: TextView = itemView.findViewById(R.id.tv_descripcion_challenge)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btn_edit_challenge)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btn_delete_challenge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challenge, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.tvDescripcion.text = challenge.description

        // Animación sutil al tocar editar (HU 6 - Criterio 7)
        holder.btnEditar.setOnClickListener {
            animarBoton(it) { onEditClick(challenge) }
        }

        // Animación sutil al tocar eliminar
        holder.btnEliminar.setOnClickListener {
            animarBoton(it) { onDeleteClick(challenge) }
        }
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
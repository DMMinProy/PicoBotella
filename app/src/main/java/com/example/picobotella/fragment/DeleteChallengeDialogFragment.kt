package com.example.picobotella.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.picobotella.R
import com.example.picobotella.database.Challenge

class DeleteChallengeDialogFragment(
    private val challenge: Challenge,
    private val onDelete: (Challenge) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_delete_challenge, null)

        dialog.setContentView(view)


        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)


        val txtDescripcion = view.findViewById<TextView>(R.id.txtDescripcion)
        val btnNo = view.findViewById<TextView>(R.id.btnNo)
        val btnSi = view.findViewById<TextView>(R.id.btnSi)


        txtDescripcion.text = challenge.description


        btnNo.setOnClickListener {
            dismiss()
        }


        btnSi.setOnClickListener {
            onDelete(challenge)
            dismiss()
        }

        return dialog
    }
}
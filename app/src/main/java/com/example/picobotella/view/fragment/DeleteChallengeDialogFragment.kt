package com.example.picobotella.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.picobotella.databinding.DialogDeleteChallengeBinding
import com.example.picobotella.model.Challenge

class DeleteChallengeDialogFragment(
    private val challenge: Challenge,
    private val onDelete: (Challenge) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogDeleteChallengeBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeleteChallengeBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = Dialog(requireContext())

        dialog.setContentView(binding.root)

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)


        binding.txtDescripcion.text = challenge.description

        binding.btnNo.setOnClickListener {
            dismiss()
        }

        binding.btnSi.setOnClickListener {
            onDelete(challenge)
            dismiss()
        }

        return dialog
    }
}
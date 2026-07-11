package com.example.picobotella.view.fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.picobotella.databinding.DialogAddChallengeBinding

class AddChallengeDialogFragment(
    private val onSave: (String) -> Unit
) : DialogFragment() {

    private var _binding: DialogAddChallengeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = DialogAddChallengeBinding.inflate(requireActivity().layoutInflater)

        binding.btnSave.isEnabled = false

        binding.etChallenge.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val enabled = !s.isNullOrBlank()
                binding.btnSave.isEnabled = enabled
                binding.btnSave.setBackgroundColor(
                    if (enabled) Color.parseColor("#FD3C00")
                    else Color.parseColor("#D9D9D9")
                )
            }
        })

        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnSave.setOnClickListener {
            onSave(binding.etChallenge.text.toString().trim())
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
            .also { it.setCanceledOnTouchOutside(false) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
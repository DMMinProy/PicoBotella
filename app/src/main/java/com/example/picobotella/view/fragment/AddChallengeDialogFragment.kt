package com.example.picobotella.view.fragment
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import com.example.picobotella.R

class AddChallengeDialogFragment (
    private val onSave: (String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = requireActivity()
            .layoutInflater
            .inflate(R.layout.dialog_add_challenge, null)

        val etChallenge = view.findViewById<TextInputEditText>(R.id.etChallenge)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        btnSave.isEnabled = false

        etChallenge.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                //Habilitar/desabilitar boton
                val enabled = !s.isNullOrBlank()

                btnSave.isEnabled = enabled

                if (enabled) {
                    btnSave.setBackgroundColor(
                        Color.parseColor("#FD3C00")
                    )
                } else {
                    btnSave.setBackgroundColor(
                        Color.parseColor("#D9D9D9")
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnSave.setOnClickListener {

            val challengeText =
                etChallenge.text.toString().trim()

            onSave(challengeText)
            dismiss()
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }
}
package com.example.picobotella

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import android.text.Editable
import android.text.TextWatcher
import com.example.picobotella.database.Challenge

class EditChallengeDialogFragment(
    private val challenge: Challenge,
    private val onSave: (Challenge) -> Unit

) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = requireActivity()
            .layoutInflater
            .inflate(R.layout.dialog_edit_challenge, null)

        val etChallenge =
            view.findViewById<TextInputEditText>(R.id.etChallenge)

        val btnCancel =
            view.findViewById<Button>(R.id.btnCancel)

        val btnSave =
            view.findViewById<Button>(R.id.btnSave)

        btnSave.isEnabled = true
        etChallenge.setText(challenge.description)

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

                btnSave.isEnabled = !s.isNullOrBlank()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnSave.setOnClickListener {

            val newText =
                etChallenge.text.toString().trim()

            onSave(
                challenge.copy(description = newText)
            )

            dismiss()
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }
}
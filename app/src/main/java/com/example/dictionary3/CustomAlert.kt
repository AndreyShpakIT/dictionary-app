package com.example.dictionary3

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.dictionary3.Word.Word

class CustomAlert(private val context: Context, private val clickListeners: AlertDialogClickListeners) {

    fun showDialog(word: Word) {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setContentView(R.layout.custom_alert_dialog)

        val text = dialog.findViewById<View>(R.id.tvWordTitle) as TextView
        text.text = word.englishWord

        /*val tvImage = dialog.findViewById<View>(R.id.ivWordState) as ImageView
        tvImage.setImageResource(word.wordState.icon)*/

        // Delete
        val deleteButton: Button = dialog.findViewById<View>(R.id.btn_dialog_delete) as Button
        deleteButton.setOnClickListener {
            clickListeners.onDeleteClickListener(word)
            dialog.dismiss()
        }

        // Change
        val changeButton: Button = dialog.findViewById<View>(R.id.btn_dialog_change) as Button
        changeButton.setOnClickListener() {
            clickListeners.onChangeClickListener(word)
            dialog.dismiss()
        }

        dialog.show()
    }
}
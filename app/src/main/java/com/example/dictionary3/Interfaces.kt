package com.example.dictionary3

import android.view.View
import com.example.dictionary3.Word.Word
import com.google.android.material.snackbar.Snackbar

// region Interfaces
interface CellListeners {
    fun onCellClickListener(data: Word, pos: Int)
}

interface AlertDialogClickListeners {
    fun onDeleteClickListener(word: Word)
    fun onChangeClickListener(word: Word)
}

interface BottomDialogOnClickListener {
    fun onBottomDialogClickListener(word: Word)
}

interface Snackbar {
    fun showSnackbar(view: View, message: String) {
        val snackbar = Snackbar.make(
                view,
                message,
                Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }
}

// endregion
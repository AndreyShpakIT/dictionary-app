package com.example.dictionary3

import com.example.dictionary3.Word.Word

// region Interfaces
interface CellListeners {
    fun onCellLongClickListener(data: Word) : Boolean
    fun onCellClickListener(data: Word)
}

interface AlertDialogClickListeners {
    fun onDeleteClickListener(word: Word)
    fun onChangeClickListener(word: Word)
}

interface BottomDialogOnClickListener {
    fun onBottomDialogClickListener(word: Word)
}

// endregion
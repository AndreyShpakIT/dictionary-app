package com.example.dictionary3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dictionary3.Word.Word
import com.example.dictionary3.databinding.BottomSheetFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomFragmentSheet(private val dialogOnClickListener: BottomDialogOnClickListener) : BottomSheetDialogFragment() {

    private val CODE = "BottomFragmentSheet"
    private lateinit var binding : BottomSheetFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = BottomSheetFragmentBinding.inflate(inflater, container, false)

        binding.buttonBottomSheetDialogAdd.setOnClickListener {
            Log.d("BottomFragmentSheet", "Button Add works...")
            val word = Word(binding.edRussianWord.text.toString(), binding.edEnglishWord.text.toString())
            dialogOnClickListener.onBottomDialogClickListener(word)
            dismiss()
        }
        return binding.root
    }
}
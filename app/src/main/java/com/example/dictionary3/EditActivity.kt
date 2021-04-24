package com.example.dictionary3

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import com.example.dictionary3.Word.Word
import com.example.dictionary3.Word.WordStates
import com.example.dictionary3.databinding.ActivityEditBinding
import com.example.dictionary3.databinding.ActivityMainBinding

class EditActivity : AppCompatActivity() {

    private val _code = "EditActivity"
    private lateinit var bindingClass: ActivityEditBinding
    private lateinit var edWord:Word
    private lateinit var spinnerAdapter: SpinnerStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingClass = ActivityEditBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        edWord = getWordFromIntent()
        spinnerAdapter = SpinnerStateAdapter(this)

        initFields()

        initHandlers()
    }

    private fun initFields() {

        bindingClass.spinner.adapter = spinnerAdapter

        val pos =
        when (edWord.wordState){
            WordStates.Green -> 0
            WordStates.Orange -> 1
            WordStates.Red -> 2
        }
        bindingClass.spinner.setSelection(pos)

        bindingClass.edEditEnglish.hint = edWord.englishWord
        bindingClass.edEditRussian.hint = edWord.russianWord

        bindingClass.edEditEnglish.setText(edWord.englishWord)
        bindingClass.edEditRussian.setText(edWord.russianWord)
    }

    private fun initHandlers() {
        bindingClass.buttonEdit.setOnClickListener {
            returnData()
        }
    }

    private fun getWordFromIntent(): Word {
        return intent.extras?.get("word") as Word
    }

    private fun returnData() {

        edWord.russianWord = bindingClass.edEditRussian.text.toString()
        edWord.englishWord = bindingClass.edEditEnglish.text.toString()
        edWord.wordState = bindingClass.spinner.selectedItem as WordStates

        val intent = Intent()
        intent.putExtra("res", edWord)
        setResult(RESULT_OK, intent)

        Log.d(_code, "returnData")

        finish()
    }
}
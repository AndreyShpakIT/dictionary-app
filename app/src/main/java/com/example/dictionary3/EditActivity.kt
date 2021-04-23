package com.example.dictionary3

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.dictionary3.Word.Word
import com.example.dictionary3.Word.WordStates
import com.example.dictionary3.databinding.ActivityEditBinding
import com.example.dictionary3.databinding.ActivityMainBinding

class EditActivity : AppCompatActivity() {

    private val _code = "EditActivity"
    private lateinit var bindingClass: ActivityEditBinding
    private lateinit var edWord:Word

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingClass = ActivityEditBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        edWord = getWordFromIntent()

        initFields()

        initHandlers()
    }

    private fun initFields() {

        //bindingClass.spinner.

        bindingClass.edEditEnglish.hint = edWord.englishWord
        bindingClass.edEditRussian.hint = edWord.russianWord

        bindingClass.edEditEnglish.setText(edWord.englishWord)
        bindingClass.edEditRussian.setText(edWord.russianWord)
        bindingClass.ivEditState.setImageResource(edWord.wordState.icon)
    }

    private fun initHandlers() {
        bindingClass.buttonEdit.setOnClickListener {
            returnData()
        }

        bindingClass.buttonEditState.setOnClickListener {

        }
    }

    private fun getWordFromIntent(): Word {
        return intent.extras?.get("word") as Word
    }

    private fun returnData() {

        edWord.russianWord = bindingClass.edEditRussian.text.toString()
        edWord.englishWord = bindingClass.edEditEnglish.text.toString()

        val intent = Intent()
        intent.putExtra("res", edWord)
        setResult(RESULT_OK, intent)

        Log.d(_code, "returnData")

        finish()
    }
}
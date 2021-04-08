package com.example.dictionary3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dictionary3.Word.Word
import com.example.dictionary3.databinding.ActivityAddBinding
import com.example.dictionary3.db.DbManager

class AddActivity : AppCompatActivity() {

    lateinit var bindingClass : ActivityAddBinding
    val db = DbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityAddBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        db.openDb()

        bindingClass.buttonAddWord.setOnClickListener() {
            if (bindingClass.editRussianWord.text.isNullOrEmpty() || bindingClass.editEnglishWord.text.isNullOrEmpty()){
                Toast.makeText(applicationContext, "Не все поля заполнены!", Toast.LENGTH_SHORT).show()
            }
            else {
                var word = Word()

                var result : Long = db.addNewWord(Word(bindingClass.editRussianWord.text.toString(), bindingClass.editEnglishWord.text.toString()))
                if (result > -1) {
                    Toast.makeText(applicationContext, "Слово добавлено!", Toast.LENGTH_SHORT).show()
                }
                else {
                    if (result == (-2).toLong()) {
                        Toast.makeText(applicationContext, "Слово уже существует", Toast.LENGTH_SHORT).show()
                        bindingClass.editEnglishWord.text.clear()
                        bindingClass.editRussianWord.text.clear()
                    }
                    else {
                        Toast.makeText(applicationContext, "Не удалось добавить слово", Toast.LENGTH_SHORT).show()
                    }
                }
                finish()
            }
        }

        bindingClass.buttonClose.setOnClickListener() {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        db.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        db.closeDb()
    }
}
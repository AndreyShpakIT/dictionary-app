package com.example.dictionary3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.example.dictionary3.Word.Word
import com.example.dictionary3.Word.WordStates
import com.example.dictionary3.databinding.ActivityTrainingBinding
import com.example.dictionary3.db.DbManager
import java.util.*
import kotlin.collections.ArrayList

class TrainingActivity : AppCompatActivity(), Snackbar {

    private lateinit var binding: ActivityTrainingBinding
    private lateinit var db: DbManager
    private lateinit var list: ArrayList<Word>
    private lateinit var spinnerAdapter: SpinnerStateAdapter
    private var currentWord: Word? = null
        set (value) {
            if (Parameters.languageEnglish == Parameters.languageRussian) {
                val rand = Random()
                if (rand.nextBoolean()) {
                    qWord = value!!.englishWord
                    tWord = value.russianWord
                }
                else {
                    qWord = value!!.russianWord
                    tWord = value.englishWord
                }
            }
            else if (Parameters.languageRussian) {
                qWord = value!!.russianWord
                tWord = value.englishWord
            }
            else {
                qWord = value!!.englishWord
                tWord = value.russianWord
            }
            field = value
        }

    private lateinit var qWord: String
    private lateinit var tWord: String
    private var nextNow = false
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DbManager(applicationContext)
        db.openDb()
        initHandlers()
        spinnerAdapter = SpinnerStateAdapter(this)
        binding.spinner2.adapter = spinnerAdapter

        loadList()
    }
    override fun onBackPressed() { }
    private fun initHandlers() {

        binding.imbClose2.setOnClickListener {
            db.closeDb()
            finish()
        }

        binding.buttonNext.setOnClickListener {
            if (nextNow) {
                setupNextWord()
            }
            else {
                showTranslate()
            }
            nextNow = !nextNow
        }

        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val sel = binding.spinner2.selectedItem as WordStates
                if (sel != currentWord!!.wordState) {
                    binding.imageView.setImageResource(sel.icon)
                    currentWord!!.wordState = sel
                    db.updateWord(currentWord!!)
                    updateStateView()
                }
            }

        }

    }





    private fun loadList() {

        if (Parameters.genType == 0) {
            db.getWordsWithStatesAsync(Parameters.dictionaryFilterGreen, Parameters.dictionaryFilterOrange, Parameters.dictionaryFilterRed)
                    .addOnSuccessListener {
                        list = it
                        setupNextWord()
                    }
                    .addOnFailureListener {
                        showSnackbar(binding.root, "Не удалось получить список слов! [1]")
                    }
        }
        else {
            db.getLearningListAsync()
                    .addOnSuccessListener {
                        list = it
                        setupNextWord()
                    }
                    .addOnFailureListener {
                        showSnackbar(binding.root, "Не удалось получить список слов! [2]")
                    }
        }
    }
    private fun setupNextWord() {
        currentWord = generateNextWord()
        binding.buttonNext.text = "Перевод"
        binding.tvTranslationWord.text = "???"
        binding.tvWord.text = qWord
        binding.imageView.setImageResource(currentWord!!.wordState.icon)
        updateStateView()

        incCount()
    }
    private fun showTranslate() {
        binding.buttonNext.text = "Следующее"
        binding.tvTranslationWord.text = tWord
    }
    private fun generateNextWord() : Word {

        var w = list.random()
        while (w == currentWord) {
            w = list.random()
        }

        return w
    }
    private fun incCount() {
        count++
        binding.tvTrainingCount.text = count.toString()
    }
    private fun updateStateView() {
        val pos =
                when (currentWord!!.wordState){
                    WordStates.Green -> 0
                    WordStates.Orange -> 1
                    WordStates.Red -> 2
                }
        binding.spinner2.setSelection(pos)
    }
}







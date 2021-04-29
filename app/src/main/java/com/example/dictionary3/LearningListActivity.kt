package com.example.dictionary3

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary3.Word.Word
import com.example.dictionary3.databinding.ActivityLearningListBinding
import com.example.dictionary3.databinding.ActivityMainBinding
import com.example.dictionary3.db.DbManager

class LearningListActivity : AppCompatActivity(), Snackbar {

    private lateinit var binding: ActivityLearningListBinding
    private lateinit var db: DbManager
    private lateinit var rcAdapter : RcAdapter
    private lateinit var wordList: ArrayList<Word>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearningListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DbManager(applicationContext)

        initializeHandlers()

        rcAdapter = RcAdapter(ArrayList(), null)

        loadAvailableWordList()

        init()
        rcAdapter.updateAdapter(db.getLearningWordList())


        db.openDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            501 -> {
                if (resultCode == Activity.RESULT_OK) {
                    handleAddWordActivity(data)
                }
            }

        }
    }

    private fun refreshRcView() {

        db.getWordListAsync()
            .addOnSuccessListener {
                //rcAdapter.updateAdapter(it)
                wordList = it
            }
            .addOnFailureListener {
                showSnackbar(binding.root,"Не удалось обратиться к базе данных")
                throw Exception("Список слов не был загружен")
            }
    }

    private fun init() {
        binding.rcViewLWords.layoutManager = LinearLayoutManager(this)
        binding.rcViewLWords.adapter = rcAdapter
    }

    private fun handleAddWordActivity(data: Intent?) {

        val word = data?.extras?.get("res") as Word

        rcAdapter.addItem(word)

        showSnackbar(binding.root, "Слово добавлено: ${word.russianWord} | ${word.englishWord} (${word.id})")

    }

    override fun onDestroy() {
        super.onDestroy()
        db.closeDb()
    }

    private fun loadAvailableWordList() {
        db.getWordListAsync()
            .addOnSuccessListener {
                intent.putExtra("availableList", it)

            }
            .addOnFailureListener {
                showSnackbar(binding.root, "Не удалось получить список слов")
            }
    }

    private fun initializeHandlers() {

        binding.buttonNewLWord.setOnClickListener {
            val intent = Intent(this, AddLWordActivity::class.java)
            startActivityForResult(intent, 501)
        }

    }





}
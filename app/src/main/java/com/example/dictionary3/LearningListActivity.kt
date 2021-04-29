package com.example.dictionary3

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary3.Word.Word
import com.example.dictionary3.Word.WordStates
import com.example.dictionary3.databinding.ActivityLearningListBinding
import com.example.dictionary3.db.DbManager
import java.util.function.Predicate

class LearningListActivity : AppCompatActivity(), Snackbar, CellListeners {

    private lateinit var binding: ActivityLearningListBinding
    private lateinit var db: DbManager
    private lateinit var rcAdapter : RcAdapter
    private lateinit var availableWordList: ArrayList<Word>

    private var restCount = 0
    private var allCount = 0

    // region Life cycle

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLearningListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRemoveLW.visibility = View.GONE
        binding.constraintLayoutTitle.visibility = View.GONE

        initHandlers()
        initRecyclerView()

        db = DbManager(applicationContext)
        db.openDb()

        refreshRc()

        loadAvailableWordList()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            501 -> {
                if (resultCode == RESULT_OK) {
                    handleAddWordActivity(data)
                }
            }

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        db.closeDb()
    }

    // endregion





    // region Initializes

    private fun initHandlers() {

        binding.buttonNewLWord.isEnabled = false

        binding.buttonNewLWord.setOnClickListener {
            val intent = Intent(this, AddLWordActivity::class.java)
            intent.putExtra("availableList", availableWordList)
            startActivityForResult(intent, 501)
        }

    }
    private fun initRecyclerView() {
        binding.rcViewLWords.layoutManager = LinearLayoutManager(this)
        rcAdapter = RcAdapter(ArrayList(), this, resources)
        binding.rcViewLWords.adapter = rcAdapter
    }

    // endregion





    // region Methods

    private fun refreshRc() {
        binding.progressBar2.visibility = View.VISIBLE
        db.getLearningListAsync()
            .addOnSuccessListener {
                binding.progressBar2.visibility = View.INVISIBLE
                rcAdapter.updateAdapter(it)

                restCount = rcAdapter.count { word ->
                    word.wordState == WordStates.Red || word.wordState == WordStates.Orange
                }
                allCount = rcAdapter.itemCount
                updateCount()

            }
            .addOnFailureListener {
                showSnackbar(binding.root, "Не удалось получить список изучаемых слов")
                binding.progressBar2.visibility = View.INVISIBLE
            }
    }
    private fun loadAvailableWordList() {

        db.getAvailableLearningListAsync()
            .addOnSuccessListener {
                availableWordList = it
                //showSnackbar(binding.root, "Список доступных слов загружен!")
                binding.buttonNewLWord.isEnabled = true
            }
            .addOnFailureListener {
                showSnackbar(binding.root, "Не удалось получить досупных список слов")
            }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun handleAddWordActivity(data: Intent?) {

        val word = data?.extras?.get("res") as Word
        rcAdapter.addItem(word)

        if (db.addNewLearningWord(word.id) < 0) {
            showSnackbar(binding.root, "Не удалось сохранить добавленное слово!")
            return
        }
        if (!availableWordList.removeIf (Predicate { t -> t.id == word.id })) {
            showSnackbar(binding.root, "Не удалось удалить слово из списка!")
        }
        else {
            allCount++
            if (word.wordState == WordStates.Red || word.wordState == WordStates.Orange) {
                restCount++
            }
            updateCount()
        }
    }
    private fun updateCount() {
        binding.tvRestCount.text = restCount.toString()
        binding.tvCountAll.text = allCount.toString()
    }

    override fun onCellClickListener(data: Word, pos: Int) {}

    override fun onCellSelected(hide: Boolean) {
        if (!hide) {
            binding.buttonNewLWord.visibility = View.GONE
            binding.constraintLayoutCount.visibility = View.GONE
            binding.buttonRemoveLW.visibility = View.VISIBLE
            binding.constraintLayoutTitle.visibility = View.VISIBLE
        }
        else {
            binding.buttonNewLWord.visibility = View.VISIBLE
            binding.constraintLayoutCount.visibility = View.VISIBLE
            binding.buttonRemoveLW.visibility = View.GONE
            binding.constraintLayoutTitle.visibility = View.GONE
        }
        binding.tvSelectedCount.text = rcAdapter.getSelectedList().size.toString()
    }

    // endregion
}






















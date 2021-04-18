package com.example.dictionary3

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary3.Word.Word
import com.example.dictionary3.databinding.FragmentHomeBinding
import com.example.dictionary3.db.DbManager
import com.example.dictionary3.db.DbNames
import com.google.android.material.snackbar.Snackbar

import kotlin.collections.ArrayList

class HomeFragment : Fragment(), CellLongClickListener, AlertDialogClickListeners, BottomDialogOnClickListener {

    private val _code: String = "HomeFragment"

    private lateinit var binding : FragmentHomeBinding
    private lateinit var rcAdapter : RcAdapter
    private lateinit var appContext: Context
    private lateinit var db: DbManager

    companion object {
        fun newInstance() = HomeFragment()
    }

    // region Life cycle

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        db = DbManager(appContext)

        // region Handlers

        binding.buttonNewWord.setOnClickListener {
            val bottomDialog = BottomFragmentSheet(this)
            bottomDialog.show(requireFragmentManager(), "TAG")
        }

        binding.buttonResfresh.setOnClickListener {
            Log.d(_code, "Refresh...")
            refreshRcView()
        }

        binding.testButton.setOnClickListener {
            db.closeDb()
            db = DbManager(appContext, DbNames.DATABASE_TEMP_NAME)
            db.openDb()
        }

        rcAdapter = RcAdapter(ArrayList(), this, this)
        init()

        // endregion

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d(_code, "OnResume()...")
        db.openDb()
        refreshRcView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        db.closeDb()
    }

    // endregion

    // region Methods

    private fun init() {
        binding.rcView.layoutManager = LinearLayoutManager(appContext)
        binding.rcView.adapter = rcAdapter
    }

    private fun refreshRcView() {
        rcAdapter.updateAdapter(db.getWordList())
    }

    override fun onCellLongClickListener(word: Word) : Boolean {

        val alert = CustomAlert(appContext, this)
        alert.showDialog(word)

        return true
    }

    override fun onDeleteClickListener(word: Word) {
        Log.d(_code, "onDeleteClickListener works...")

        val snackbar = Snackbar.make(
            binding.root,
            "Вы уверены что хотите удалить слово: ${word.englishWord}?",
            Snackbar.LENGTH_LONG
        )

        snackbar.setAction(
            "Yes"
        ) {
            db.deleteWord(word)
            refreshRcView()
        }
        snackbar.show()
    }

    override fun onChangeClickListener(word: Word) {

        Log.d(_code, "onChangeClickListener works...")
    }

    override fun onBottomDialogClickListener(word: Word) {

        if (word.russianWord.isNullOrEmpty() || word.englishWord.isNullOrEmpty()) {
            Toast.makeText(appContext, "Не все поля заполнены!", Toast.LENGTH_SHORT).show()
        }
        else {
            val result : Long = db.addNewWord(word)
            if (result > -1) {
                Toast.makeText(appContext, "Слово добавлено!", Toast.LENGTH_SHORT).show()
                refreshRcView()
            }
            else {
                if (result == (-2).toLong()) {
                    Toast.makeText(appContext, "Слово уже существует", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(appContext, "Не удалось добавить слово", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // endregion

}





















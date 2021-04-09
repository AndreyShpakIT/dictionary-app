package com.example.dictionary3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary3.Word.Word
import com.example.dictionary3.databinding.ActivityMainBinding
import com.example.dictionary3.db.DbManager
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(), CellLongClickListener, AlertDialogClickListeners {

    private val CODE: String = "MainActivity"
    private lateinit var bindingClass : ActivityMainBinding
    private lateinit var rcAdapter : RcAdapter
    private val db = DbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        bindingClass.buttonNewWord.setOnClickListener {
            /*val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)*/

            var bottomDialog = BottomFragmentSheet()
            bottomDialog.show(supportFragmentManager, "TAG")

        }

        bindingClass.buttonResfresh.setOnClickListener() {
            refreshRcView()
        }

        rcAdapter = RcAdapter(ArrayList(), this, this)

        db.openDb()
        init()
    }

    override fun onResume() {
        super.onResume()
        db.openDb()
        refreshRcView()
    }

    override fun onDestroy() {
        super.onDestroy()
        db.closeDb()
    }

    private fun init() {
        bindingClass.rcView.layoutManager = LinearLayoutManager(this)
        bindingClass.rcView.adapter = rcAdapter
    }

    private fun refreshRcView() {
        rcAdapter.updateAdapter(db.getWordList())
    }

    override fun onCellLongClickListener(word: Word) : Boolean {

        var alert = CustomAlert(this, this)
        alert.showDialog(word)

        return true
    }

    override fun onDeleteClickListener(word: Word) {
        Log.d(CODE, "onDeleteClickListener works...")

        val snackbar = Snackbar.make(
            bindingClass.root,
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

        Log.d(CODE, "onChangeClickListener works...")
    }

}

interface CellLongClickListener {
    fun onCellLongClickListener(data: Word) : Boolean
}

interface AlertDialogClickListeners {
    fun onDeleteClickListener(word: Word)
    fun onChangeClickListener(word: Word)
}

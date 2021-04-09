package com.example.dictionary3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary3.databinding.ActivityMainBinding
import com.example.dictionary3.db.DbManager
import com.example.dictionary3.db.RcAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var bindingClass : ActivityMainBinding
    private val rcAdapter = RcAdapter(ArrayList())
    private val db = DbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        bindingClass.buttonNewWord.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }



        bindingClass.buttonTest.setOnClickListener {



        }

        db.openDb()
        init()
    }

    override fun onResume() {
        super.onResume()
        db.openDb()
        fillAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        db.closeDb()
    }

    private fun init() {
        bindingClass.rcView.layoutManager = LinearLayoutManager(this)
        bindingClass.rcView.adapter = rcAdapter
    }

    private fun fillAdapter() {
        rcAdapter.updateAdapter(db.getWordList())
    }
}
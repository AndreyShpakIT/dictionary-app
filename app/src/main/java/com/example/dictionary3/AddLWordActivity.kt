package com.example.dictionary3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary3.Word.Word
import com.example.dictionary3.Word.WordStates
import com.example.dictionary3.databinding.ActivityAddLWordBinding

class AddLWordActivity : AppCompatActivity(), CellListeners {

    lateinit var binding: ActivityAddLWordBinding
    lateinit var rcAdapter: RcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddLWordBinding.inflate(layoutInflater)

        setContentView(binding.root)

        rcAdapter = RcAdapter(ArrayList(), this)

        init()
        val list = getDataFromIntent()
        rcAdapter.updateAdapter(list)
    }

    private fun getDataFromIntent(): ArrayList<Word> {
        return intent.extras?.get("availableList") as ArrayList<Word>
    }

    private fun init() {
        binding.rcEnabledLWords.layoutManager = LinearLayoutManager(this)
        binding.rcEnabledLWords.adapter = rcAdapter
    }

    override fun onCellClickListener(data: Word, pos: Int) {

        returnData(data)
        finish()

    }

    private fun returnData(data: Word) {

        val intent = Intent()
        intent.putExtra("res", data)
        setResult(RESULT_OK, intent)

        finish()
    }

}
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
    lateinit var list: ArrayList<Word>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initHandlers()

        list = intent.extras?.get("availableList") as ArrayList<Word>
        filterListByState()
    }


    override fun onBackPressed() {
        //super.onBackPressed()
    }

    private fun initHandlers() {
        binding.chbGreen.setOnCheckedChangeListener { _, _ ->
            filterListByState()
        }
        binding.chbOrange.setOnCheckedChangeListener { _, _ ->
            filterListByState()
        }
        binding.chbRed.setOnCheckedChangeListener { _, _ ->
            filterListByState()
        }
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }


    private fun initRecyclerView() {
        rcAdapter = RcAdapter(ArrayList(), this, false)
        binding.rcEnabledLWords.layoutManager = LinearLayoutManager(this)
        binding.rcEnabledLWords.adapter = rcAdapter
    }


    override fun onCellClickListener(data: Word, pos: Int) {
        val intent = Intent()
        intent.putExtra("res", data)
        setResult(RESULT_OK, intent)

        finish()
    }
    private fun filterListByState() {

        val green = binding.chbGreen.isChecked
        val orange = binding.chbOrange.isChecked
        val red = binding.chbRed.isChecked

        if (green == orange && green == red){
            refreshItems(list)
            return
        }

        val filteredList = ArrayList<Word>()

        for (word in list) {
                if (green && word.wordState == WordStates.Green ||
                    orange && word.wordState == WordStates.Orange ||
                    red && word.wordState == WordStates.Red)
                    {
                    filteredList.add(word)
                }
        }

        refreshItems(filteredList)
    }
    private fun refreshItems(list: ArrayList<Word>) {

        rcAdapter.updateAdapter(list)
        binding.tvAllCount2.text = list.size.toString()
    }
}





















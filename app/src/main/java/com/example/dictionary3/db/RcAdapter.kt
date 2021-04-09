package com.example.dictionary3.db

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary3.R
import com.example.dictionary3.Word.Word
import com.example.dictionary3.databinding.RcItemBinding

class RcAdapter(wordList: ArrayList<Word>) : RecyclerView.Adapter<RcAdapter.Holder>() {

    var list:ArrayList<Word> = wordList

    class Holder(binding: RcItemBinding) : RecyclerView.ViewHolder(binding.root) {

        var binding = binding

        fun setData(word: Word){
            binding.tvEnglish.text = word.englishWord
            binding.tvRussian.text = word.russianWord
            binding.tvState.text = word.wordState.name
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RcItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.setOnClickListener() {
/*
            Toast.makeText(parent.context, "${binding.tvRussian.text} | ${binding.tvEnglish.text} | ${binding.tvState.text}", Toast.LENGTH_SHORT).show()
*/
            val builder = android.app.AlertDialog.Builder(parent.context)
            builder
                    .setView(R.layout.custom_alert_dialog)

            builder.show()
        }

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateAdapter(listItems:List<Word>){
        list.clear()
        list.addAll(listItems)
        notifyDataSetChanged()
    }

}
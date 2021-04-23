package com.example.dictionary3

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary3.Word.Word
import com.example.dictionary3.databinding.RcCardItemBinding

class RcAdapter(private var list: ArrayList<Word>, private var context: Fragment, private val cellClickListener: CellListeners) : RecyclerView.Adapter<RcAdapter.Holder>() {

    class Holder(private var binding: RcCardItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(word: Word){
            binding.tvEnglish.text = word.englishWord
            binding.tvRussian.text = word.russianWord
            binding.ivState.setImageResource(word.wordState.icon)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding = RcCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.setData(list[position])

        holder.itemView.setOnLongClickListener {
            cellClickListener.onCellLongClickListener(list[position])
        }

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(list[position], position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateAdapter(listItems:List<Word>){
        list.clear()
        list.addAll(listItems)
        notifyDataSetChanged()
    }

    fun updateItem(word: Word, position: Int){
        list[position] = word
        notifyItemChanged(position)
        //notifyDataSetChanged()
    }
}
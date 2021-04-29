package com.example.dictionary3

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary3.Word.Word
import com.example.dictionary3.databinding.RcItemBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.RestrictsSuspension

class RcAdapter(private var list: ArrayList<Word>, private val cellClickListener: CellListeners?) : RecyclerView.Adapter<RcAdapter.Holder>(), Filterable {

    class Holder(private var binding: RcItemBinding) : RecyclerView.ViewHolder(binding.root) {



        fun setData(word: Word) {
            binding.tvEnglish.text = word.englishWord
            binding.tvRussian.text = word.russianWord

            binding.ivState.setImageResource(word.wordState.icon)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding = RcItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.setData(list[position])

        holder.itemView.setOnClickListener {
            cellClickListener?.onCellClickListener(list[position], position)
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
    }

    fun addItem(word: Word) {
        list.add(word)
        notifyItemInserted(list.size - 1)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterString = constraint.toString().toLowerCase(Locale.ROOT)
                if (filterString.isEmpty()) {

                }
                else {
                    val filteredList = ArrayList<Word>()
                    for (word in list){
                        if (word.russianWord.toLowerCase(Locale.ROOT).contains(filterString) ||
                                word.englishWord.toLowerCase(Locale.ROOT).contains(filterString)) {
                            filteredList.add(word)
                        }
                    }
                    list = filteredList
                }
                val res = FilterResults()
                res.values = list

                return res
            }

            @Suppress
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                list = results?.values as ArrayList<Word>
                notifyDataSetChanged()
            }
        }
    }

}
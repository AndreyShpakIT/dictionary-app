package com.example.dictionary3

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary3.Word.Word
import com.example.dictionary3.databinding.RcItemBinding
import java.util.*
import kotlin.collections.ArrayList

class RcAdapter(private var list: ArrayList<Word>, private val cellClickListener: CellListeners?, private val selectable: Boolean) : RecyclerView.Adapter<RcAdapter.Holder>(), Filterable {

    class Holder(var binding: RcItemBinding, private var listeners: CellListeners?, private var selectable: Boolean) : RecyclerView.ViewHolder(binding.root) {

        fun bind(word: Word, pos: Int) {

            binding.tvEnglish.text = word.englishWord
            binding.tvRussian.text = word.russianWord
            binding.ivState.setImageResource(word.wordState.icon)

            if (selectable) {

                if (word.isSelected) {
                    binding.rcItemCard.setCardBackgroundColor(ResourceManager.getSelectedBackgroundColor())
                } else {
                    binding.rcItemCard.setCardBackgroundColor(ResourceManager.getUnselectedBackgroundColor())
                }

                itemView.setOnClickListener {
                    if (word.isSelected) {
                        binding.rcItemCard.setCardBackgroundColor(ResourceManager.getUnselectedBackgroundColor())
                    } else {
                        binding.rcItemCard.setCardBackgroundColor(ResourceManager.getSelectedBackgroundColor())
                    }
                    word.isSelected = !word.isSelected
                    listeners?.onCellSelected()
                }
            } else {
                itemView.setOnClickListener {
                    listeners?.onCellClickListener(word, pos)
                }
            }
        }
    }

    // region Default

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RcItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, cellClickListener, selectable)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(list[position], position)

    }
    override fun getItemCount(): Int {
        return list.size
    }


    // endregion


    fun getSelectedList() : ArrayList<Int> {
        val selectedList = ArrayList<Int>()

        list.forEachIndexed { i, w ->
            if (w.isSelected) {
                selectedList.add(i)
            }
        }
        /*for (item in list) {
            if (item.isSelected) {
                selectedList.add(item)
            }
        }*/
        return selectedList
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

    fun updateItem(word: Word, position: Int) {
        list[position] = word
        notifyItemChanged(position)
    }
    fun updateAdapter(listItems:List<Word>) {
        list.clear()
        list.addAll(listItems)
        notifyDataSetChanged()
    }
    fun updateItemsView() {
        notifyDataSetChanged()
    }

    fun addItem(word: Word) {
        list.add(word)
        notifyItemInserted(list.size - 1)
    }
    fun count(predicate: (Word) -> (Boolean)) : Int {
        var count = 0
        for (item in list) {
            if (predicate(item)) {
                count++
            }
        }
        return count
    }

    fun removeIf(predicate: (Word) -> (Boolean)) : Int {
        var count = 0
        list.forEachIndexed { index, word ->
          if (predicate(word)) {
              list.removeAt(index)
              count++
              notifyItemRemoved(index)
          }
        }
        return count
    }
    fun removeAt(pos: Int) {
        list.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun getItem(pos: Int) : Word = list[pos]
}
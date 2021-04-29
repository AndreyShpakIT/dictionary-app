package com.example.dictionary3

import android.content.res.Resources
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary3.Word.Word
import com.example.dictionary3.databinding.RcItemBinding
import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList
import kotlin.coroutines.RestrictsSuspension

class RcAdapter(private var list: ArrayList<Word>, private val cellClickListener: CellListeners?, private var resources: Resources? = null) : RecyclerView.Adapter<RcAdapter.Holder>(), Filterable {

    class Holder(var binding: RcItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(word: Word) {
            binding.tvEnglish.text = word.englishWord
            binding.tvRussian.text = word.russianWord
            binding.ivState.setImageResource(word.wordState.icon)

        }

    }

    // region Default

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

        if (resources != null) {
            holder.itemView.setOnClickListener {
                if (resources != null) {
                    if (list[position].isSelected) {

                        holder.binding.rcItemCard.setCardBackgroundColor(ResourcesCompat.getColor(resources!!, R.color.card_light_background, null))
                        list[position].isSelected = false

                        cellClickListener?.onCellSelected(getSelectedList().size == 0)
                    }
                    else {
                        holder.binding.rcItemCard.setCardBackgroundColor(ResourcesCompat.getColor(resources!!, R.color.card_light_background_selected, null))
                        list[position].isSelected = true
                        cellClickListener?.onCellSelected(false)
                    }
                }
            }
        }


    }
    override fun getItemCount(): Int {
        return list.size
    }


    // endregion


    fun getSelectedList() : ArrayList<Word> {
        val selectedList = ArrayList<Word>()
        for (item in list) {
            if (item.isSelected) {
                selectedList.add(item)
            }
        }
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
}
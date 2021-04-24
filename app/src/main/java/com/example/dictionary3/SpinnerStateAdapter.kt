package com.example.dictionary3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.dictionary3.Word.WordStates

class SpinnerStateAdapter(val context: Context) : BaseAdapter() {

    private val list = arrayOf(WordStates.Green, WordStates.Orange, WordStates.Red)
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.spinner_state_item, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        val name =
        when (list[position]) {
            WordStates.Green -> "Изучено"
            WordStates.Orange -> "Знакомо"
            WordStates.Red -> "Не изучено"
        }
        vh.label.text = name
        vh.img.setBackgroundResource(list[position].icon)

        return view
    }

    override fun getItem(position: Int): Any? {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private class ItemHolder(row: View?) {

        var label: TextView = row?.findViewById(R.id.tvSpinnerState) as TextView
        var img: ImageView = row?.findViewById(R.id.ivSpinnerState) as ImageView

    }

}

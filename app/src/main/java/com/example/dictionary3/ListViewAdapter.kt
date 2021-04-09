package com.example.dictionary3

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.dictionary3.Word.Word


class ListViewAdapter(private val context: Context, private val items: ArrayList<Word>) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.rc_item, parent, false)

        val tvRus = convertView?.findViewById(R.id.tvRussian) as TextView
        val tvEng = convertView?.findViewById(R.id.tvEnglish) as TextView
        val tvState = convertView?.findViewById(R.id.ivState) as ImageView

        tvRus.text = items[position].russianWord
        tvEng.text = items[position].englishWord
        tvState.setImageResource(items[position].wordState.icon)

        return convertView
    }


    fun updateAdapter(listItems:List<Word>){
        items.clear()
        items.addAll(listItems)
        notifyDataSetChanged()
    }
}
package com.example.dictionary3.Word

import com.example.dictionary3.R


enum class WordStates(name: String, public var icon: Int) {
    Green("Green", R.drawable.state_green), Orange("Orange", R.drawable.state_orange), Red("Red", R.drawable.state_red);

    companion object{
        fun convertToWordState(name: String?) : WordStates {
            return when (name) {
                "Green" -> Green
                "Orange" -> Orange
                "Yellow" -> Orange
                "Red" -> Red
                else -> throw Exception("Статус не определен")
            }
        }
        fun convertToWordState(iconId: Int) : WordStates {
            return when (iconId) {
                R.drawable.state_green -> Green
                R.drawable.state_orange -> Orange
                R.drawable.state_red -> Red
                else -> throw Exception("Статус не определен")
            }
        }
    }

}
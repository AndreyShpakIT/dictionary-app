package com.example.dictionary3.Word

enum class WordStates(name: String) {
    Green("Green"), Yellow("Yellow"), Red("Red");

    companion object{
        fun convertToWordState(name: String?) : WordStates {
            return when (name) {
                "Green" -> Green
                "Yellow" -> Yellow
                else -> Red
            }
        }
    }

}
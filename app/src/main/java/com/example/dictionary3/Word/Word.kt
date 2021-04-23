package com.example.dictionary3.Word

import java.io.Serializable

class Word : Serializable {

    var russianWord: String = ""
        set(value) {
            field = toFormat(value)
        }
    var englishWord: String = ""
        set(value) {
            field = toFormat(value)
        }
    var wordState: WordStates = WordStates.Red

    constructor(russian: String, english: String, state: WordStates = WordStates.Red){
        russianWord = russian
        englishWord = english
        wordState = state
    }

    constructor()

    constructor(word: Word) {
        russianWord = word.russianWord
        englishWord = word.englishWord
        wordState = word.wordState
    }

    private fun toFormat(string: String) : String {
        var s: String  = string
        s = string.trim()
        s = s.toLowerCase()
        s = s.capitalize()
        return s
    }
}
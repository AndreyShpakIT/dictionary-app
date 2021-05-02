package com.example.dictionary3

import android.content.SharedPreferences


object ParametersName {

    const val P_NAME = "mycfg"
    const val P_THEME = "dark"
    const val P_FILTER_GREEN = "DictionaryFilterGreen"
    const val P_FILTER_ORANGE = "DictionaryFilterOrange"
    const val P_FILTER_RED = "DictionaryFilterRed"
    const val P_GEN_TYPE = "GenType"
    const val P_LANG_ENG = "LangEng"
    const val P_LANG_RUS = "LangRus"
}

class Parameters {

    companion object {
        
        var perf: SharedPreferences? = null
            set (value) {
                editor = value?.edit()
                if (editor == null) {
                    throw Exception("editor is null on init")
                }
                field = value
            }

        private var editor = perf?.edit()

        var darkTheme = false
            set(value) {
                editor?.putBoolean(ParametersName.P_THEME, value)
                field = value
            }

        var dictionaryFilterGreen = false
            set(value) {
                editor?.putBoolean(ParametersName.P_FILTER_GREEN, value)
                field = value
            }
        var dictionaryFilterOrange = true
            set(value) {
                editor?.putBoolean(ParametersName.P_FILTER_ORANGE, value)
                field = value
            }

        var dictionaryFilterRed = true
            set(value) {
                editor?.putBoolean(ParametersName.P_FILTER_RED, value)
                field = value
            }


        // 0 - dictionary
        // 1 - learning list
        var genType = 0
            set(value) {
                editor?.putInt(ParametersName.P_GEN_TYPE, value)
                field = value
            }

        var languageRussian = true
            set(value) {
                editor?.putBoolean(ParametersName.P_LANG_RUS, value)
                field = value
            }
        var languageEnglish = true
            set(value) {
                editor?.putBoolean(ParametersName.P_LANG_ENG, value)
                field = value
            }

        fun save() {
            editor?.apply() ?: throw Exception("editor is null")
        }
        fun load() {
            if (perf != null) {
                darkTheme = perf!!.getBoolean(ParametersName.P_THEME, false)
                dictionaryFilterGreen = perf!!.getBoolean(ParametersName.P_FILTER_GREEN, false)
                dictionaryFilterOrange = perf!!.getBoolean(ParametersName.P_FILTER_ORANGE, true)
                dictionaryFilterRed = perf!!.getBoolean(ParametersName.P_FILTER_RED, true)
                genType = perf!!.getInt(ParametersName.P_GEN_TYPE, 0)
                languageEnglish = perf!!.getBoolean(ParametersName.P_LANG_ENG, true)
                languageRussian = perf!!.getBoolean(ParametersName.P_LANG_RUS, true)
            }
            else {
                throw Exception("null on load")
            }
        }
    }
}


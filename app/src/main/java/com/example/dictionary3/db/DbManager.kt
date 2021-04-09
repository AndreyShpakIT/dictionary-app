package com.example.dictionary3.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.dictionary3.Word.Word
import com.example.dictionary3.Word.WordStates

class DbManager(val context: Context) {

    private val dbHelper = DbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        dbHelper.onCreate(db)
        db = dbHelper.writableDatabase
    }

    fun closeDb(){
        dbHelper.close()
    }

    fun addNewWord(word: Word) : Long {

        return if (!wordExists(word)) {
            val values = ContentValues().apply {
                put(DbNames.FIELD_RUSSIAN, word.russianWord)
                put(DbNames.FIELD_ENGLISH, word.englishWord)
                put(DbNames.FIELD_STATE, word.wordState.name)
            }

            db?.insert(DbNames.TABLE_WORDS, null, values)!! -1

        } else {
            -2
        }
    }

    fun wordExists(word: Word) : Boolean {

        val selection = "${DbNames.FIELD_ENGLISH} = ? AND ${DbNames.FIELD_RUSSIAN} = ?"
        val selectionArgs = arrayOf("${word.englishWord}", "${word.russianWord}")

        val cursor = db?.query(DbNames.TABLE_WORDS, null, selection, selectionArgs, null, null, null)

        return cursor?.count ?: -1 > 0
    }

    fun getWordList() : ArrayList<Word> {
        val dataList = ArrayList<Word>()

        val cursor = db?.query(DbNames.TABLE_WORDS, null, null, null, null, null, null)

        var russianWord: String = ""
        var englishWord: String = ""
        var wordState: WordStates = WordStates.Red


        with(cursor) {
            while (this?.moveToNext()!!) {

                russianWord = getString(getColumnIndex(DbNames.FIELD_RUSSIAN)) ?: "-"
                englishWord = getString(getColumnIndex(DbNames.FIELD_ENGLISH)) ?: "-"
                wordState = WordStates.convertToWordState(getString(getColumnIndex(DbNames.FIELD_STATE)))

                dataList.add(Word(russianWord, englishWord, wordState))
            }
        }

        cursor?.close()
        return dataList
    }

    fun deleteWord(word: Word) {

        val selection = "${DbNames.FIELD_RUSSIAN} = ? AND ${DbNames.FIELD_ENGLISH} = ?"
        val selectionArguments = arrayOf<String>(word.russianWord, word.englishWord)

        val deletedWords = db?.delete(DbNames.TABLE_WORDS, selection, selectionArguments)
    }

    fun clearDb() {
        db?.delete(DbNames.TABLE_WORDS, null, null)
    }

    fun dropDb() {
        db?.execSQL(DbNames.SQL_DELETE_WORDS)
        db?.execSQL(DbNames.SQL_DELETE_LWORDS)
    }

}
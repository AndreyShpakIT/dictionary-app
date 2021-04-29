package com.example.dictionary3.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.UserDictionary
import android.util.Log
import com.example.dictionary3.Word.Word
import com.example.dictionary3.Word.WordStates
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DbManager(val context: Context, private val dbName: String = DbNames.DATABASE_NAME) {

    private val mExecutor: Executor = Executors.newSingleThreadExecutor()

    private val _code = "DbManager"
    private val dbHelper = DbHelper(context, dbName)
    var db: SQLiteDatabase? = null

    fun openDb() {
        dbHelper.onCreate(db)
        db = dbHelper.writableDatabase

        Log.w(_code, "Открыто соединение с базой данных: dbName: $dbName")
    }

    fun closeDb() {
        dbHelper.close()
        Log.w(_code, "Закрыто соединение с базой данных: dbName: $dbName")
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

    private fun wordExists(word: Word) : Boolean {

        val selection = "${DbNames.FIELD_ENGLISH} = ? AND ${DbNames.FIELD_RUSSIAN} = ?"
        val selectionArgs = arrayOf(word.englishWord, word.russianWord)

        val cursor = db?.query(DbNames.TABLE_WORDS, null, selection, selectionArgs, null, null, null)

        return cursor?.count ?: -1 > 0
    }

    fun getWordList() : ArrayList<Word> {
        val dataList = ArrayList<Word>()

        val cursor = db?.query(DbNames.TABLE_WORDS, null, null, null, null, null, null)

        var russianWord: String = ""
        var englishWord: String = ""
        var wordState: WordStates = WordStates.Red
        var id = -1

        with(cursor) {
            while (this?.moveToNext()!!) {

                russianWord = getString(getColumnIndex(DbNames.FIELD_RUSSIAN)) ?: "-"
                englishWord = getString(getColumnIndex(DbNames.FIELD_ENGLISH)) ?: "-"
                wordState = WordStates.convertToWordState(getString(getColumnIndex(DbNames.FIELD_STATE)))
                id = getInt(getColumnIndex(DbNames.FIELD_WORD_ID)) ?: -1

                dataList.add(Word(russianWord, englishWord, wordState, id))
            }
        }

        cursor?.close()
        return dataList
    }

    fun getLearningWordList() : ArrayList<Word> {
        val dataList = ArrayList<Word>()

        val cursor = db?.query(DbNames.TABLE_LWORDS, null, null, null, null, null, null)

        var russianWord: String = ""
        var englishWord: String = ""
        var wordState: WordStates = WordStates.Red
        var id = -1

        if (cursor == null)
        {
            cursor?.close()
            return dataList
        }

        with(cursor) {
            while (this.moveToNext()) {

                russianWord = getString(getColumnIndex(DbNames.FIELD_RUSSIAN)) ?: "-"
                englishWord = getString(getColumnIndex(DbNames.FIELD_ENGLISH)) ?: "-"
                wordState = WordStates.convertToWordState(getString(getColumnIndex(DbNames.FIELD_STATE)))
                id = getInt(getColumnIndex(DbNames.FIELD_WORD_ID)) ?: -1

                dataList.add(Word(russianWord, englishWord, wordState, id))

            }
        }

        cursor.close()
        return dataList
    }

    fun getEnabledLearningWordList() : ArrayList<Word> {
        val dataList = ArrayList<Word>()

        val cursor = db?.query(DbNames.TABLE_WORDS, null, null, null, null, null, null)

        var russianWord: String = ""
        var englishWord: String = ""
        var wordState: WordStates = WordStates.Red
        var id = -1

        with(cursor) {
            while (this?.moveToNext()!!) {

                russianWord = getString(getColumnIndex(DbNames.FIELD_RUSSIAN)) ?: "-"
                englishWord = getString(getColumnIndex(DbNames.FIELD_ENGLISH)) ?: "-"
                wordState = WordStates.convertToWordState(getString(getColumnIndex(DbNames.FIELD_STATE)))
                id = getInt(getColumnIndex(DbNames.FIELD_WORD_ID)) ?: -1

                dataList.add(Word(russianWord, englishWord, wordState, id))
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

    fun updateWord(word: Word) {

        val contentValues = ContentValues()
        contentValues.put(DbNames.FIELD_ENGLISH, word.englishWord)
        contentValues.put(DbNames.FIELD_RUSSIAN, word.russianWord)
        contentValues.put(DbNames.FIELD_STATE, word.wordState.name)

        db?.update(DbNames.TABLE_WORDS, contentValues, "${DbNames.FIELD_WORD_ID } = ?", arrayOf(word.id.toString()))
    }

    fun clearDb() {
        db?.delete(DbNames.TABLE_WORDS, null, null)
        Log.w(_code, "База данных очищена")
    }

    fun dropDb() {
        db?.execSQL(DbNames.SQL_DELETE_WORDS)
        db?.execSQL(DbNames.SQL_DELETE_LWORDS)
        Log.w(_code, "База дынных удалена")
    }

    fun getWordListAsync() : Task<ArrayList<Word>> {

        return Tasks.call(mExecutor, {

            val dataList = ArrayList<Word>()

            val cursor = db?.query(DbNames.TABLE_WORDS, null, null, null, null, null, null)

            var russianWord: String = ""
            var englishWord: String = ""
            var wordState: WordStates = WordStates.Red
            var id = -1

            with(cursor) {
                while (this?.moveToNext()!!) {

                    russianWord = getString(getColumnIndex(DbNames.FIELD_RUSSIAN)) ?: "-"
                    englishWord = getString(getColumnIndex(DbNames.FIELD_ENGLISH)) ?: "-"
                    wordState = WordStates.convertToWordState(getString(getColumnIndex(DbNames.FIELD_STATE)))
                    id = getInt(getColumnIndex(DbNames.FIELD_WORD_ID)) ?: -1

                    dataList.add(Word(russianWord, englishWord, wordState, id))
                }
            }

            cursor?.close()
            return@call dataList

        })
    }

}
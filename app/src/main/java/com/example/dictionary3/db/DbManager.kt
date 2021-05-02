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





    // region Private

    private fun wordExists(word: Word) : Boolean {

        val selection = "${DbNames.FIELD_ENGLISH} = ? AND ${DbNames.FIELD_RUSSIAN} = ?"
        val selectionArgs = arrayOf(word.englishWord, word.russianWord)

        val cursor = db?.query(DbNames.TABLE_WORDS, null, selection, selectionArgs, null, null, null)

        return cursor?.count ?: -1 > 0
    }
    private fun getWordList() : ArrayList<Word> {
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
    private fun getLearningList() : ArrayList<Word> {

        val sql =
                "SELECT W.${DbNames.FIELD_WORD_ID}, W.${DbNames.FIELD_RUSSIAN}, W.${DbNames.FIELD_ENGLISH}, W.${DbNames.FIELD_STATE} FROM ${DbNames.TABLE_LWORDS} AS LW " +
                "JOIN ${DbNames.TABLE_WORDS} AS W " +
                "ON W.${DbNames.FIELD_WORD_ID} = LW.${DbNames.FIELD_LWORDS_ID}"

        return executeQuery(sql)
    }
    private fun getAvailableLearningList() : ArrayList<Word> {

        val sql =
                "SELECT W.* FROM ${DbNames.TABLE_WORDS} AS W " +
                "EXCEPT " +
                "SELECT W2.* FROM ${DbNames.TABLE_LWORDS} AS LW " +
                "JOIN ${DbNames.TABLE_WORDS} AS W2 " +
                "ON W2.${DbNames.FIELD_WORD_ID} = LW.${DbNames.FIELD_LWORDS_ID}"

        return executeQuery(sql)
    }
    private fun executeQuery(sql: String, includeId: Boolean = true) : ArrayList<Word> {

        val cursor = db?.rawQuery(sql, null)

        var russianWord = ""
        var englishWord = ""
        var id = -1
        var wordState = WordStates.Red

        val dataList = ArrayList<Word>()

        with(cursor) {
            while (this?.moveToNext()!!) {

                russianWord = getString(getColumnIndex(com.example.dictionary3.db.DbNames.FIELD_RUSSIAN)) ?: "-"
                englishWord = getString(getColumnIndex(com.example.dictionary3.db.DbNames.FIELD_ENGLISH)) ?: "-"
                wordState = com.example.dictionary3.Word.WordStates.convertToWordState(getString(getColumnIndex(com.example.dictionary3.db.DbNames.FIELD_STATE)))
                if (includeId)
                    id = getInt(getColumnIndex(com.example.dictionary3.db.DbNames.FIELD_WORD_ID)) ?: -1

                dataList.add(Word(russianWord, englishWord, wordState, id))
            }
        }

        return dataList
    }
    private fun getWordsWithStates(green: Boolean, orange: Boolean, red: Boolean) : ArrayList<Word> {
        val dataList = ArrayList<Word>()

        val selection = "${DbNames.FIELD_STATE} = ? OR ${DbNames.FIELD_STATE} = ? OR ${DbNames.FIELD_STATE} = ?"

        val greenState = if (green) "Green" else "-"
        val orangeState = if (orange) "Yellow" else "-"
        val redState = if (red) "Red" else "-"

        val selectionArgs = arrayOf(greenState, orangeState, redState)

        val cursor = db?.query(DbNames.TABLE_WORDS, null, selection, selectionArgs, null, null, null)

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

    // endregion





    // region Public

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
    fun addNewLearningWord(id: Int) : Long {

        val values = ContentValues().apply {
            put(DbNames.FIELD_LWORDS_ID, id.toString())
        }

        return db?.insert(DbNames.TABLE_LWORDS, null, values)!! -1
    }
    fun deleteLearningWord(word: Word) : Long {

        var selection = ""
        var selectionArguments = arrayOf<String>()

        if (word.id > 0) {
            selection = "${DbNames.FIELD_WORD_ID} = ?"
            selectionArguments = arrayOf<String>(word.id.toString())
        }
        else {
            selection = "${DbNames.FIELD_RUSSIAN} = ? AND ${DbNames.FIELD_ENGLISH} = ?"
            selectionArguments = arrayOf<String>(word.russianWord, word.englishWord)
        }
        return db?.delete(DbNames.TABLE_WORDS, selection, selectionArguments)?.toLong() ?: -1
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

    fun getWordListAsync() : Task<ArrayList<Word>> {

        return Tasks.call(mExecutor, {
            return@call getWordList()
        })
    }
    fun getLearningListAsync() : Task<ArrayList<Word>> {

        return Tasks.call(mExecutor, {
            return@call getLearningList()
        })
    }
    fun getAvailableLearningListAsync() : Task<ArrayList<Word>> {
        return Tasks.call(mExecutor, {
            return@call getAvailableLearningList()
        })
    }
    fun getWordsWithStatesAsync(green: Boolean, orange: Boolean, red: Boolean) : Task<ArrayList<Word>> {
        return Tasks.call(mExecutor, {
            return@call getWordsWithStates(green, orange, red)
        })
    }

    // endregion





    // region Manage

    fun openDb() {
        dbHelper.onCreate(db)
        db = dbHelper.writableDatabase

        Log.w(_code, "Открыто соединение с базой данных: dbName: $dbName")
    }
    fun closeDb() {
        dbHelper.close()
        Log.w(_code, "Закрыто соединение с базой данных: dbName: $dbName")
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

    // endregion
}




















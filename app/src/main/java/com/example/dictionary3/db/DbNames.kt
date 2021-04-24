package com.example.dictionary3.db

import android.provider.BaseColumns

object DbNames : BaseColumns {

    // Data Base information
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "Words.db"
    const val DATABASE_TEMP_NAME = "TempWords.db"
    const val DRIVE_DB_NAME = "AndroidWords.db"
    const val DATABASE_MIMETYPE = "application/vnd.sqlite3"

    const val DATABASE_PATH = "/data/data/com.example.dictionary3/databases/${DbNames.DATABASE_NAME}"
    const val DATABASE_TEMP_PATH = "/data/data/com.example.dictionary3/databases/${DbNames.DATABASE_TEMP_NAME}"

    // Tables
    const val TABLE_WORDS = "Words"
    const val TABLE_LWORDS = "LearningWords"

    // Words fields
    const val FIELD_WORD_ID = "WordID"
    const val FIELD_RUSSIAN = "RussianWord"
    const val FIELD_ENGLISH = "EnglishWord"
    const val FIELD_STATE = "State"

    // Learning words fields
    const val FIELD_LWORDS_ID = "WordID"

    // SQL queries
    const val SQL_CREATE_TABLE_WORDS =
        "CREATE TABLE IF NOT EXISTS $TABLE_WORDS(" +
                "$FIELD_WORD_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "$FIELD_RUSSIAN TEXT , " +
                "$FIELD_ENGLISH TEXT, " +
                "$FIELD_STATE TEXT)"

    const val SQL_CREATE_TABLE_LWORDS =
        "CREATE TABLE IF NOT EXISTS $TABLE_LWORDS(" +
                "$FIELD_LWORDS_ID INTEGER REFERENCES Words (WordID))"

    const val SQL_DELETE_WORDS = "DROP TABLE IF EXISTS $TABLE_WORDS"
    const val SQL_DELETE_LWORDS = "DROP TABLE IF EXISTS $TABLE_LWORDS"
}
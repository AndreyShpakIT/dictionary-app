package com.example.dictionary3.db

import android.provider.BaseColumns

object DbNames : BaseColumns {

    // Data Base information
    const val DATABASE_NAME = "Words.db"
    const val DATABASE_VERSION = 1

    // Tables
    const val TABLE_WORDS = "Words"
    const val TABLE_LWORDS = "LearningWords"

    // Words fields
    const val FIELD_WORDS_ID = "WordID"
    const val FIELD_RUSSIAN = "RussianWord"
    const val FIELD_ENGLISH = "EnglishWord"
    const val FIELD_STATE = "State"

    // Learning words fields
    const val FIELD_LWORDS_ID = "WordID"

    // SQL queries
    const val SQL_CREATE_TABLE_WORDS =
        "CREATE TABLE IF NOT EXISTS $TABLE_WORDS(" +
                "$FIELD_WORDS_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "$FIELD_RUSSIAN TEXT , " +
                "$FIELD_ENGLISH TEXT, " +
                "$FIELD_STATE TEXT)"

    const val SQL_CREATE_TABLE_LWORDS =
        "CREATE TABLE IF NOT EXISTS $TABLE_LWORDS(" +
                "$FIELD_LWORDS_ID INTEGER REFERENCES Words (WordID))"

    const val SQL_DELETE_WORDS = "DROP TABLE IF EXISTS $TABLE_WORDS"
    const val SQL_DELETE_LWORDS = "DROP TABLE IF EXISTS $TABLE_LWORDS"
}
package com.example.dictionary3.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DbNames.DATABASE_NAME, null, DbNames.DATABASE_VERSION ) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DbNames.SQL_CREATE_TABLE_WORDS)
        db?.execSQL(DbNames.SQL_CREATE_TABLE_LWORDS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL(DbNames.SQL_DELETE_WORDS)
        db?.execSQL(DbNames.SQL_DELETE_LWORDS)

        onCreate(db)
    }

}
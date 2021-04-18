package com.example.dictionary3.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DbHelper(context: Context, private val dbName: String) : SQLiteOpenHelper(context, dbName, null, DbNames.DATABASE_VERSION ) {

    private val _code = "DbHelper"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DbNames.SQL_CREATE_TABLE_WORDS)
        db?.execSQL(DbNames.SQL_CREATE_TABLE_LWORDS)
        //Log.d(_code, "OnCreate()...dbName: $dbName")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL(DbNames.SQL_DELETE_WORDS)
        db?.execSQL(DbNames.SQL_DELETE_LWORDS)

        onCreate(db)
    }

}
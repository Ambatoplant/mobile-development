package com.dev.ambatoplant.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery =
            "CREATE TABLE IF NOT EXISTS analisis (id INTEGER PRIMARY KEY, result TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS analisis")
        onCreate(db)
    }

    fun insertAnalysis(result: String) {
        val values = ContentValues()
        values.put("result", result)
        val db = writableDatabase
        db.insert("analisis", null, values)
    }

    fun getAllAnalyses(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM analisis", null)
    }
    companion object {
        const val DATABASE_NAME = "PlantAnalysis.db"
        const val DATABASE_VERSION = 1
    }

}

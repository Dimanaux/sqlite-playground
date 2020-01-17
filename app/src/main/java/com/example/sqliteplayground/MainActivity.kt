package com.example.sqliteplayground

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent

class MainActivity : AppCompatActivity() {
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = openOrCreateDatabase("default", Context.MODE_PRIVATE, null)
        db.execSQL(
            """CREATE TABLE IF NOT EXISTS visits (
                id INTEGER PRIMARY KEY,
                time DATETIME DEFAULT CURRENT_TIMESTAMP
            );""".trimIndent()
        )
        db.execSQL("INSERT INTO visits DEFAULT VALUES;")

        setContent {
            SQLitePlayground {
                try {
                    query(it)
                } catch (e: SQLiteException) {
                    Relation.error(e)
                }
            }
        }
    }

    private fun query(query: String): Relation {
        val cursor = db.rawQuery(query, null)
        val header = cursor.columnNames.asList()
        val body = mutableListOf<List<String>>()

        while (cursor.moveToNext()) {
            body.add(cursor.rowAsStrings())
        }
        cursor.close()
        return Relation("Result", header, body)
    }

    private fun Cursor.rowAsStrings(): List<String> {
        val row = mutableListOf<String>()
        for (i in (0 until columnCount)) {
            val value = getString(i)
            row.add(value)
        }
        return row
    }
}

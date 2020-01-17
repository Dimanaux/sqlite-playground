package com.example.sqliteplayground

import android.database.sqlite.SQLiteException

data class Relation(
    val name: String,
    val header: List<String>,
    val rows: List<List<String>>
) {
    fun isEmpty() = header.isEmpty()
    fun isNotEmpty() = header.isNotEmpty()

    companion object {
        fun error(error: SQLiteException) =
            Relation(
                "Error",
                listOf("error_type", "message"),
                listOf(listOf(error.javaClass.simpleName, error.message ?: "<No message>"))
            )
    }
}

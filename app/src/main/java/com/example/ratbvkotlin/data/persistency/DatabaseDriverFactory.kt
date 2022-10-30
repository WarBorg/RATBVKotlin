package com.example.ratbvkotlin.data.persistency

import android.content.Context
import com.example.ratbvkotlin.RatbvDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

class DatabaseDriverFactory(private val context: Context) {
    fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            RatbvDatabase.Schema,
            context, "ratbv_delight.db")
    }
}
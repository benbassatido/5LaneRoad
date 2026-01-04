package com.example.hw2.utilities

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager private constructor(context: Context) {

    companion object {
        private const val PREFS_NAME = "ScorePrefs"

        @Volatile
        private var instance: SharedPreferencesManager? = null

        fun init(context: Context): SharedPreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesManager(context.applicationContext).also { instance = it }
            }
        }

        fun getInstance(): SharedPreferencesManager {
            return instance ?: throw IllegalStateException(
                "SharedPreferencesManager must be initialized by calling init(context) before use."
            )
        }
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, default: String? = null): String? {
        return prefs.getString(key, default)
    }

    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}

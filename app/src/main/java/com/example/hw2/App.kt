package com.example.hw2

import android.app.Application
import com.example.hw2.utilities.SharedPreferencesManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        SharedPreferencesManager.init(this)
    }
}

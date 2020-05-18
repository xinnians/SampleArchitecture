package com.example.base

import android.app.Application
import android.content.Context

class SharedPreferencesProvider(private var application: Application) {

    fun sharedPreferences() = application.getSharedPreferences(NAME, Context.MODE_PRIVATE)!!

    companion object {
        private const val NAME = "SharedPreferencesData"
    }
}
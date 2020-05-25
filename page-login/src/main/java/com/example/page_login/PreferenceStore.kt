package com.example.page_login

import android.content.Context
import androidx.core.content.edit

class PreferenceStore(context: Context) {

    companion object {
        const val TOKEN = "token"
        const val REFRESH_TOKEN = "refreshToken"
        const val EXPIRATION_AT = "expirationAt"
        const val SE_SWITCH = "seSwitch"
        const val ACCOUNT = "account"
        const val PASSWORD = "password"
        const val REMEMBER = "remember"
    }

    private val prefs = context.applicationContext.getSharedPreferences("test", Context.MODE_PRIVATE)

    // String
    var token: String
        get() = prefs.getString(TOKEN, "")!!
        set(token) = prefs.edit { putString(TOKEN, token) }

    var refreshToken: String
        get() = prefs.getString(REFRESH_TOKEN, "")!!
        set(refreshToken) = prefs.edit { putString(REFRESH_TOKEN, refreshToken) }

    var expirationAt: Long
        get() = prefs.getLong(EXPIRATION_AT, 0L)
        set(expirationAt) = prefs.edit { putLong(EXPIRATION_AT, expirationAt) }

    var seSwitch: Boolean
        get() = prefs.getBoolean(SE_SWITCH, false)
        set(switch) = prefs.edit { putBoolean(SE_SWITCH, switch) }

    var account: String
        get() = prefs.getString(ACCOUNT, "test123")!!
        set(account) = prefs.edit { putString(ACCOUNT, account) }

    var password: String
        get() = prefs.getString(PASSWORD, "test123")!!
        set(password) = prefs.edit { putString(PASSWORD, password) }

    var remember: Boolean
        get() = prefs.getBoolean(REMEMBER, false)
        set(remember) = prefs.edit() {putBoolean(REMEMBER, remember)}
}
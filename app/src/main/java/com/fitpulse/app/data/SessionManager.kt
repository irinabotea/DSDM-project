package com.fitpulse.app.data

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveLogin(username: String) {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_USERNAME, username)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_LOGGED_IN, false)

    fun getUsername(): String = prefs.getString(KEY_USERNAME, "") ?: ""

    fun logout() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "fitpulse_session"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_USERNAME = "username"
    }
}

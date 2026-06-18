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

    fun saveProfile(profile: UserProfile) {
        prefs.edit()
            .putString(KEY_WEIGHT, profile.weightKg)
            .putString(KEY_HEIGHT, profile.heightCm)
            .putString(KEY_AGE, profile.age)
            .putString(KEY_GENDER, profile.gender)
            .putString(KEY_GOAL, profile.goal)
            .apply()
    }

    fun getProfile(): UserProfile = UserProfile(
        weightKg = prefs.getString(KEY_WEIGHT, "") ?: "",
        heightCm = prefs.getString(KEY_HEIGHT, "") ?: "",
        age = prefs.getString(KEY_AGE, "") ?: "",
        gender = prefs.getString(KEY_GENDER, "") ?: "",
        goal = prefs.getString(KEY_GOAL, "") ?: ""
    )

    fun logout() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "fitpulse_session"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_USERNAME = "username"
        private const val KEY_WEIGHT = "weight_kg"
        private const val KEY_HEIGHT = "height_cm"
        private const val KEY_AGE = "age"
        private const val KEY_GENDER = "gender"
        private const val KEY_GOAL = "goal"
    }
}

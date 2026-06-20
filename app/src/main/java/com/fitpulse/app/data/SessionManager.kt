package com.fitpulse.app.data

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveLogin(userId: Long, username: String, email: String) {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putLong(KEY_USER_ID, userId)
            .putString(KEY_USERNAME, username)
            .putString(KEY_EMAIL, email)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_LOGGED_IN, false)

    fun getUserId(): Long = prefs.getLong(KEY_USER_ID, -1L)

    fun getUsername(): String = prefs.getString(KEY_USERNAME, "") ?: ""

    fun saveProfile(profile: UserProfile) {
        val id = getUserId()
        prefs.edit()
            .putString(keyFor(KEY_WEIGHT, id), profile.weightKg)
            .putString(keyFor(KEY_HEIGHT, id), profile.heightCm)
            .putString(keyFor(KEY_AGE, id), profile.age)
            .putString(keyFor(KEY_GENDER, id), profile.gender)
            .putString(keyFor(KEY_GOAL, id), profile.goal)
            .apply()
    }

    fun getProfile(): UserProfile {
        val id = getUserId()
        return UserProfile(
            weightKg = prefs.getString(keyFor(KEY_WEIGHT, id), "") ?: "",
            heightCm = prefs.getString(keyFor(KEY_HEIGHT, id), "") ?: "",
            age = prefs.getString(keyFor(KEY_AGE, id), "") ?: "",
            gender = prefs.getString(keyFor(KEY_GENDER, id), "") ?: "",
            goal = prefs.getString(keyFor(KEY_GOAL, id), "") ?: ""
        )
    }

    /** Clears only the session — per-user profile data is preserved. */
    fun logout() {
        prefs.edit()
            .remove(KEY_LOGGED_IN)
            .remove(KEY_USER_ID)
            .remove(KEY_USERNAME)
            .remove(KEY_EMAIL)
            .apply()
    }

    private fun keyFor(base: String, userId: Long): String = "${base}_$userId"

    companion object {
        private const val PREFS_NAME = "fitpulse_session"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_WEIGHT = "weight_kg"
        private const val KEY_HEIGHT = "height_cm"
        private const val KEY_AGE = "age"
        private const val KEY_GENDER = "gender"
        private const val KEY_GOAL = "goal"
    }
}

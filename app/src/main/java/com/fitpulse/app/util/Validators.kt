package com.fitpulse.app.util

object Validators {

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() &&
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidUsername(username: String): Boolean {
        return username.trim().length >= 3
    }
}

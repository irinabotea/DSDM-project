package com.fitpulse.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fitpulse.app.data.AppDatabase
import com.fitpulse.app.data.SessionManager
import com.fitpulse.app.data.User
import com.fitpulse.app.util.PasswordHasher
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getInstance(application).userDao()
    private val sessionManager = SessionManager(application)

    fun register(
        username: String,
        email: String,
        password: String,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        val normalizedEmail = email.trim().lowercase()
        viewModelScope.launch {
            try {
                if (userDao.countByEmail(normalizedEmail) > 0) {
                    onError("Email already registered")
                    return@launch
                }
                val salt = PasswordHasher.generateSalt()
                val hash = PasswordHasher.hash(password, salt)
                val newId = userDao.insert(
                    User(
                        username = username.trim(),
                        email = normalizedEmail,
                        passwordHash = hash,
                        salt = salt
                    )
                )
                sessionManager.saveLogin(newId, username.trim(), normalizedEmail)
                onSuccess()
            } catch (e: Exception) {
                onError("Could not create account. Please try again.")
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        val normalizedEmail = email.trim().lowercase()
        viewModelScope.launch {
            try {
                val user = userDao.getByEmail(normalizedEmail)
                if (user == null) {
                    onError("Invalid email or password")
                    return@launch
                }
                val hash = PasswordHasher.hash(password, user.salt)
                if (hash != user.passwordHash) {
                    onError("Invalid email or password")
                    return@launch
                }
                sessionManager.saveLogin(user.id, user.username, user.email)
                onSuccess()
            } catch (e: Exception) {
                onError("Could not log in. Please try again.")
            }
        }
    }
}

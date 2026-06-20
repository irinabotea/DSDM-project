package com.fitpulse.app.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fitpulse.app.data.AppDatabase
import com.fitpulse.app.data.Exercise
import com.fitpulse.app.data.ExerciseRepository
import com.fitpulse.app.data.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExerciseRepository
    private val sessionManager = SessionManager(application)
    private val userId = MutableStateFlow(sessionManager.getUserId())

    init {
        val dao = AppDatabase.getInstance(application).exerciseDao()
        repository = ExerciseRepository(dao)
    }

    /** Re-reads the logged-in user so the list reflects the current account. */
    fun refreshUser() {
        val current = sessionManager.getUserId()
        if (current != userId.value) {
            userId.value = current
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val exercises: StateFlow<List<Exercise>> = userId
        .flatMapLatest { id -> repository.exercisesForUser(id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addExercise(
        name: String,
        muscleGroup: String,
        category: String,
        trackingType: String,
        sets: Int,
        reps: Int,
        durationMinutes: Int,
        date: Long
    ) {
        viewModelScope.launch {
            try {
                repository.insert(
                    Exercise(
                        userId = userId.value,
                        name = name,
                        muscleGroup = muscleGroup,
                        category = category,
                        trackingType = trackingType,
                        sets = sets,
                        reps = reps,
                        durationMinutes = durationMinutes,
                        date = date
                    )
                )
            } catch (e: Exception) {
                Log.e("FitPulse", "Failed to insert exercise", e)
            }
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            try {
                repository.delete(exercise)
            } catch (e: Exception) {
                Log.e("FitPulse", "Failed to delete exercise", e)
            }
        }
    }
}
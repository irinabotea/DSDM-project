package com.fitpulse.app.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fitpulse.app.data.AppDatabase
import com.fitpulse.app.data.Exercise
import com.fitpulse.app.data.ExerciseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExerciseRepository

    init {
        val dao = AppDatabase.getInstance(application).exerciseDao()
        repository = ExerciseRepository(dao)
    }

    val exercises: StateFlow<List<Exercise>> = repository.exercises
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addExercise(
        name: String,
        muscleGroup: String,
        category: String,
        sets: Int,
        reps: Int,
        date: Long
    ) {
        viewModelScope.launch {
            try {
                repository.insert(
                    Exercise(
                        name = name,
                        muscleGroup = muscleGroup,
                        category = category,
                        sets = sets,
                        reps = reps,
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
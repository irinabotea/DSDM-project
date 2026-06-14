package com.fitpulse.app.ui.viewmodel

import android.app.Application
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

    fun addExercise(name: String, muscleGroup: String, sets: Int, reps: Int) {
        viewModelScope.launch {
            repository.insert(
                Exercise(
                    name = name,
                    muscleGroup = muscleGroup,
                    sets = sets,
                    reps = reps
                )
            )
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.delete(exercise)
        }
    }
}

package com.fitpulse.app.data

import kotlinx.coroutines.flow.Flow

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    val exercises: Flow<List<Exercise>> = exerciseDao.getAll()

    suspend fun count(): Int = exerciseDao.count()

    suspend fun insert(exercise: Exercise) = exerciseDao.insert(exercise)

    suspend fun delete(exercise: Exercise) = exerciseDao.delete(exercise)
}

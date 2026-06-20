package com.fitpulse.app.data

import kotlinx.coroutines.flow.Flow

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    fun exercisesForUser(userId: Long): Flow<List<Exercise>> =
        exerciseDao.getForUser(userId)

    suspend fun count(userId: Long): Int = exerciseDao.count(userId)

    suspend fun insert(exercise: Exercise) = exerciseDao.insert(exercise)

    suspend fun delete(exercise: Exercise) = exerciseDao.delete(exercise)
}

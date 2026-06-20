package com.fitpulse.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercises WHERE userId = :userId ORDER BY date DESC, id DESC")
    fun getForUser(userId: Long): Flow<List<Exercise>>

    @Query("SELECT COUNT(*) FROM exercises WHERE userId = :userId")
    suspend fun count(userId: Long): Int

    @Insert
    suspend fun insert(exercise: Exercise)

    @Delete
    suspend fun delete(exercise: Exercise)
}

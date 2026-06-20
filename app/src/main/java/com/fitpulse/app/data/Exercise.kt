package com.fitpulse.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long = 0,
    val name: String,
    val muscleGroup: String,
    val category: String = "",
    val trackingType: String = "REPS",
    val sets: Int = 0,
    val reps: Int = 0,
    val durationMinutes: Int = 0,
    val date: Long = System.currentTimeMillis()
)

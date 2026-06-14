package com.fitpulse.app.data

data class Exercise(
    val id: Int,
    val name: String,
    val category: String,
    val sets: Int,
    val reps: Int,
    val weight: Double
)
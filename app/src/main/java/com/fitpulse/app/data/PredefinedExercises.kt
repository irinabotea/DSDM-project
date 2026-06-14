package com.fitpulse.app.data

data class PredefinedExercise(
    val name: String,
    val muscleGroup: String
)

object PredefinedExercises {
    val all: List<PredefinedExercise> = listOf(
        PredefinedExercise("Push-ups", "Chest"),
        PredefinedExercise("Bench Press", "Chest"),
        PredefinedExercise("Pull-ups", "Back"),
        PredefinedExercise("Deadlift", "Back"),
        PredefinedExercise("Squats", "Legs"),
        PredefinedExercise("Lunges", "Legs"),
        PredefinedExercise("Shoulder Press", "Shoulders"),
        PredefinedExercise("Bicep Curls", "Arms"),
        PredefinedExercise("Triceps Dips", "Arms"),
        PredefinedExercise("Plank", "Core"),
        PredefinedExercise("Crunches", "Core"),
        PredefinedExercise("Running", "Cardio")
    )
}

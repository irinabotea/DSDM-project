package com.fitpulse.app.data

data class PredefinedExercise(
    val name: String,
    val muscleGroup: String,
    val category: String
)

object PredefinedExercises {
    val all: List<PredefinedExercise> = listOf(
        PredefinedExercise("Push-ups", "Chest", "Chest"),
        PredefinedExercise("Bench Press", "Chest", "Chest"),
        PredefinedExercise("Pull-ups", "Lats", "Back"),
        PredefinedExercise("Deadlift", "Back", "Back"),
        PredefinedExercise("Squats", "Quads", "Legs"),
        PredefinedExercise("Lunges", "Quads", "Legs"),
        PredefinedExercise("Shoulder Press", "Shoulders", "Shoulders"),
        PredefinedExercise("Bicep Curls", "Biceps", "Arms"),
        PredefinedExercise("Triceps Dips", "Triceps", "Arms"),
        PredefinedExercise("Plank", "Abs", "Abs"),
        PredefinedExercise("Crunches", "Abs", "Abs"),
        PredefinedExercise("Running", "Calves", "Cardio")
    )
}

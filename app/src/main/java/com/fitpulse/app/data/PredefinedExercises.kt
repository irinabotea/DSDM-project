package com.fitpulse.app.data

data class PredefinedExercise(
    val name: String,
    val muscleGroup: String,
    val category: String,
    val trackingType: TrackingType = TrackingType.REPS
)

object PredefinedExercises {
    val all: List<PredefinedExercise> = listOf(
        // Rep-based strength exercises
        PredefinedExercise("Push-ups", "Chest", "Chest"),
        PredefinedExercise("Bench Press", "Chest", "Chest"),
        PredefinedExercise("Chest Fly", "Chest", "Chest"),
        PredefinedExercise("Pull-ups", "Lats", "Back"),
        PredefinedExercise("Deadlift", "Back", "Back"),
        PredefinedExercise("Lat Pulldown", "Lats", "Back"),
        PredefinedExercise("Dumbbell Row", "Back", "Back"),
        PredefinedExercise("Squats", "Quads", "Legs"),
        PredefinedExercise("Lunges", "Quads", "Legs"),
        PredefinedExercise("Leg Press", "Quads", "Legs"),
        PredefinedExercise("Calf Raises", "Calves", "Legs"),
        PredefinedExercise("Shoulder Press", "Shoulders", "Shoulders"),
        PredefinedExercise("Bicep Curls", "Biceps", "Arms"),
        PredefinedExercise("Triceps Dips", "Triceps", "Arms"),
        PredefinedExercise("Crunches", "Abs", "Abs"),
        // Time-based exercises
        PredefinedExercise("Running", "Calves", "Cardio", TrackingType.TIME),
        PredefinedExercise("Cycling", "Quads", "Cardio", TrackingType.TIME),
        PredefinedExercise("Walking", "Calves", "Cardio", TrackingType.TIME),
        PredefinedExercise("Jump Rope", "Calves", "Cardio", TrackingType.TIME),
        PredefinedExercise("Swimming", "Lats", "Cardio", TrackingType.TIME),
        PredefinedExercise("Rowing", "Back", "Cardio", TrackingType.TIME),
        PredefinedExercise("HIIT", "Quads", "Cardio", TrackingType.TIME),
        PredefinedExercise("Plank", "Abs", "Abs", TrackingType.TIME),
        PredefinedExercise("Stretching", "Abs", "Mobility", TrackingType.TIME),
        PredefinedExercise("Yoga", "Abs", "Mobility", TrackingType.TIME)
    )
}

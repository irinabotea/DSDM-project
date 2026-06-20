package com.fitpulse.app.data

import kotlin.math.roundToInt

data class CalorieAdvice(
    val maintenance: Int,
    val target: Int,
    val tip: String
)

data class UserProfile(
    val weightKg: String = "",
    val heightCm: String = "",
    val age: String = "",
    val gender: String = "",
    val goal: String = ""
) {
    fun bmi(): Double? {
        val weight = weightKg.toDoubleOrNull()
        val heightMeters = heightCm.toDoubleOrNull()?.div(100)
        if (weight == null || heightMeters == null || heightMeters <= 0) return null
        return weight / (heightMeters * heightMeters)
    }

    /**
     * Daily calorie suggestion based on the user's body metrics and fitness goal.
     * Uses the Mifflin-St Jeor BMR, a light-activity factor, then a goal adjustment.
     * Returns null when the profile is not complete enough to compute it.
     */
    fun calorieAdvice(): CalorieAdvice? {
        val weight = weightKg.toDoubleOrNull()
        val height = heightCm.toDoubleOrNull()
        val years = age.toIntOrNull()
        if (weight == null || height == null || years == null) return null
        if (weight <= 0 || height <= 0 || years <= 0) return null
        if (gender.isBlank() || goal.isBlank()) return null

        // Mifflin-St Jeor basal metabolic rate
        val bmr = when (gender) {
            "Male" -> 10 * weight + 6.25 * height - 5 * years + 5
            "Female" -> 10 * weight + 6.25 * height - 5 * years - 161
            else -> 10 * weight + 6.25 * height - 5 * years - 78 // neutral average
        }
        val maintenance = (bmr * LIGHT_ACTIVITY_FACTOR).roundToInt()

        val (rawTarget, tip) = when (goal) {
            "Lose weight" -> (maintenance - 500) to
                "A moderate deficit of about 500 kcal/day supports steady fat loss."
            "Build muscle" -> (maintenance + 300) to
                "A small surplus of about 300 kcal/day supports muscle growth."
            else -> maintenance to
                "Eating around maintenance keeps your weight stable."
        }
        // Never suggest a dangerously low intake
        val target = rawTarget.coerceAtLeast(1200)
        return CalorieAdvice(maintenance = maintenance, target = target, tip = tip)
    }

    companion object {
        val genders = listOf("Male", "Female", "Other")
        val goals = listOf("Lose weight", "Maintain", "Build muscle")

        private const val LIGHT_ACTIVITY_FACTOR = 1.375

        private const val MIN_WEIGHT = 20.0
        private const val MAX_WEIGHT = 400.0
        private const val MIN_HEIGHT = 50.0
        private const val MAX_HEIGHT = 260.0
        private const val MIN_AGE = 1
        private const val MAX_AGE = 120

        fun bmiCategory(bmi: Double): String = when {
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Normal"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }

        /** Returns an error message for the weight, or null if it is valid (empty is allowed). */
        fun weightError(value: String): String? {
            if (value.isBlank()) return null
            val n = value.toDoubleOrNull() ?: return "Weight must be a number"
            if (n < MIN_WEIGHT || n > MAX_WEIGHT) {
                return "Weight must be between ${MIN_WEIGHT.toInt()} and ${MAX_WEIGHT.toInt()} kg"
            }
            return null
        }

        fun heightError(value: String): String? {
            if (value.isBlank()) return null
            val n = value.toDoubleOrNull() ?: return "Height must be a number"
            if (n < MIN_HEIGHT || n > MAX_HEIGHT) {
                return "Height must be between ${MIN_HEIGHT.toInt()} and ${MAX_HEIGHT.toInt()} cm"
            }
            return null
        }

        fun ageError(value: String): String? {
            if (value.isBlank()) return null
            val n = value.toIntOrNull() ?: return "Age must be a whole number"
            if (n < MIN_AGE || n > MAX_AGE) {
                return "Age must be between $MIN_AGE and $MAX_AGE"
            }
            return null
        }
    }
}

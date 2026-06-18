package com.fitpulse.app.data

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

    companion object {
        val genders = listOf("Male", "Female", "Other")
        val goals = listOf("Lose weight", "Maintain", "Build muscle")

        fun bmiCategory(bmi: Double): String = when {
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Normal"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }
    }
}

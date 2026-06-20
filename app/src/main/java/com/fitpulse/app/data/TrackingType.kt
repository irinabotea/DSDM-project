package com.fitpulse.app.data

enum class TrackingType {
    REPS,
    TIME;

    companion object {
        fun fromName(value: String): TrackingType =
            entries.firstOrNull { it.name == value } ?: REPS
    }
}

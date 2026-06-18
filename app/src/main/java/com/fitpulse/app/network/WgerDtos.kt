package com.fitpulse.app.network

data class WgerListResponse<T>(
    val count: Int,
    val results: List<T>
)

data class WgerCategory(
    val id: Int,
    val name: String
)

data class WgerMuscle(
    val id: Int,
    val name: String,
    val name_en: String
) {
    /** Common English name when available, otherwise the Latin name. */
    val displayName: String
        get() = if (name_en.isNotBlank()) name_en else name
}

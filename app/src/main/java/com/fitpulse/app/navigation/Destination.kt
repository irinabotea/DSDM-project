package com.fitpulse.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(val route: String, val label: String) {
    data object Login : Destination("login", "Login")
    data object Register : Destination("register", "Register")
    data object Home : Destination("home", "Home")
    data object ExerciseList : Destination("exerciseList", "Exercises")
    data object AddExercise : Destination("addExercise", "Add Exercise")
    data object Statistics : Destination("statistics", "Statistics")
    data object Profile : Destination("profile", "Profile")
}

data class BottomNavItem(
    val destination: Destination,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Destination.Home, Icons.Filled.Home),
    BottomNavItem(Destination.ExerciseList, Icons.AutoMirrored.Filled.List),
    BottomNavItem(Destination.Statistics, Icons.Filled.DateRange),
    BottomNavItem(Destination.Profile, Icons.Filled.Person)
)

package com.fitpulse.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fitpulse.app.ui.screens.HomeScreen
import com.fitpulse.app.ui.screens.LoginScreen
import com.fitpulse.app.ui.screens.PlaceholderScreen
import com.fitpulse.app.ui.screens.RegisterScreen

private val bottomBarRoutes = bottomNavItems.map { it.destination.route }.toSet()

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navigateToTab: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.route in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.destination.route
                        } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = { navigateToTab(item.destination.route) },
                            icon = {
                                Icon(item.icon, contentDescription = item.destination.label)
                            },
                            label = { Text(item.destination.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Destination.Home.route) {
                            popUpTo(Destination.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Destination.Register.route) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Destination.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Destination.Home.route) {
                            popUpTo(Destination.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Destination.Login.route) {
                            popUpTo(Destination.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Destination.Home.route) {
                HomeScreen(
                    onMyExercises = { navigateToTab(Destination.ExerciseList.route) },
                    onStatistics = { navigateToTab(Destination.Statistics.route) }
                )
            }
            composable(Destination.ExerciseList.route) {
                PlaceholderScreen(
                    title = Destination.ExerciseList.label,
                    subtitle = "Your exercises will appear here"
                )
            }
            composable(Destination.AddExercise.route) {
                PlaceholderScreen(
                    title = Destination.AddExercise.label,
                    subtitle = "Add a new exercise"
                )
            }
            composable(Destination.Statistics.route) {
                PlaceholderScreen(
                    title = Destination.Statistics.label,
                    subtitle = "Your progress statistics will appear here"
                )
            }
            composable(Destination.Profile.route) {
                PlaceholderScreen(
                    title = Destination.Profile.label,
                    subtitle = "Your profile"
                )
            }
        }
    }
}

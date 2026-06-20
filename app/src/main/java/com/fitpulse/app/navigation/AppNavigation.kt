package com.fitpulse.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fitpulse.app.data.SessionManager
import com.fitpulse.app.ui.screens.AddExerciseScreen
import com.fitpulse.app.ui.screens.ExerciseListScreen
import com.fitpulse.app.ui.screens.HomeScreen
import com.fitpulse.app.ui.screens.LoginScreen
import com.fitpulse.app.ui.screens.PlaceholderScreen
import com.fitpulse.app.ui.screens.ProfileScreen
import com.fitpulse.app.ui.screens.RegisterScreen
import com.fitpulse.app.ui.viewmodel.AuthViewModel
import com.fitpulse.app.ui.viewmodel.ExerciseViewModel
import com.fitpulse.app.ui.viewmodel.MuscleGroupViewModel
import com.fitpulse.app.ui.screens.StatisticsScreen

private val bottomBarRoutes = bottomNavItems.map { it.destination.route }.toSet()

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val startDestination = remember {
        if (sessionManager.isLoggedIn()) Destination.Home.route else Destination.Login.route
    }

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
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Login.route) {
                val authViewModel: AuthViewModel = viewModel()
                var authError by remember { mutableStateOf<String?>(null) }
                LoginScreen(
                    authError = authError,
                    onLogin = { email, password ->
                        authError = null
                        authViewModel.login(
                            email = email,
                            password = password,
                            onError = { authError = it },
                            onSuccess = {
                                navController.navigate(Destination.Home.route) {
                                    popUpTo(Destination.Login.route) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )
                    },
                    onNavigateToRegister = {
                        navController.navigate(Destination.Register.route) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Destination.Register.route) {
                val authViewModel: AuthViewModel = viewModel()
                var authError by remember { mutableStateOf<String?>(null) }
                RegisterScreen(
                    authError = authError,
                    onRegister = { username, email, password ->
                        authError = null
                        authViewModel.register(
                            username = username,
                            email = email,
                            password = password,
                            onError = { authError = it },
                            onSuccess = {
                                navController.navigate(Destination.Home.route) {
                                    popUpTo(Destination.Login.route) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )
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
                val viewModel: ExerciseViewModel = viewModel()
                LaunchedEffect(Unit) { viewModel.refreshUser() }
                val exercises by viewModel.exercises.collectAsState()
                ExerciseListScreen(
                    exercises = exercises,
                    onAddExercise = {
                        navController.navigate(Destination.AddExercise.route)
                    }
                )
            }
            composable(Destination.AddExercise.route) {
                val viewModel: ExerciseViewModel = viewModel()
                LaunchedEffect(Unit) { viewModel.refreshUser() }
                val muscleGroupViewModel: MuscleGroupViewModel = viewModel()
                val muscleGroupState by muscleGroupViewModel.uiState.collectAsState()
                AddExerciseScreen(
                    muscleGroupState = muscleGroupState,
                    onBack = { navController.popBackStack() },
                    onRetryMuscleGroups = { muscleGroupViewModel.load() },
                    onSave = { name, muscleGroup, category, trackingType, sets, reps, duration, date ->
                        viewModel.addExercise(
                            name, muscleGroup, category, trackingType, sets, reps, duration, date
                        )
                        navController.popBackStack()
                    }
                )
            }
            composable(Destination.Statistics.route) {
                val viewModel: ExerciseViewModel = viewModel()
                LaunchedEffect(Unit) { viewModel.refreshUser() }
                val exercises by viewModel.exercises.collectAsState()

                StatisticsScreen(
                    exercises = exercises
                )
            }
            composable(Destination.Profile.route) {
                ProfileScreen(
                    username = sessionManager.getUsername(),
                    profile = sessionManager.getProfile(),
                    onSaveProfile = { sessionManager.saveProfile(it) },
                    onLogout = {
                        sessionManager.logout()
                        navController.navigate(Destination.Login.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
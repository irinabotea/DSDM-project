package com.fitpulse.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fitpulse.app.data.Exercise
import com.fitpulse.app.ui.screens.AddExerciseScreen
import com.fitpulse.app.ui.screens.ExerciseListScreen
import com.fitpulse.app.ui.screens.HomeScreen
import com.fitpulse.app.ui.screens.LoginScreen
import com.fitpulse.app.ui.screens.RegisterScreen
import com.fitpulse.app.ui.screens.StatisticsScreen
import com.fitpulse.app.ui.theme.FitPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            FitPulseApp()
        }
    }
}

@Composable
fun FitPulseApp() {
    val navController = rememberNavController()
    val exercises = remember { mutableStateListOf<Exercise>() }

    FitPulseTheme {
        Scaffold { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("login") {
                    LoginScreen(
                        onLoginClick = {
                            navController.navigate("home")
                        },
                        onRegisterClick = {
                            navController.navigate("register")
                        }
                    )
                }

                composable("register") {
                    RegisterScreen(
                        onRegisterClick = {
                            navController.navigate("home")
                        },
                        onBackToLoginClick = {
                            navController.popBackStack()
                        }
                    )
                }

                composable("home") {
                    HomeScreen(
                        onMyExercises = {
                            navController.navigate("exercises")
                        },
                        onStatistics = {
                            navController.navigate("statistics")
                        }
                    )
                }

                composable("exercises") {
                    ExerciseListScreen(
                        exercises = exercises,
                        onAddExerciseClick = {
                            navController.navigate("add_exercise")
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }

                composable("add_exercise") {
                    AddExerciseScreen(
                        onSaveExercise = { exercise ->
                            exercises.add(exercise)
                            navController.popBackStack()
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }

                composable("statistics") {
                    StatisticsScreen(
                        exercises = exercises,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FitPulseAppPreview() {
    FitPulseApp()
}
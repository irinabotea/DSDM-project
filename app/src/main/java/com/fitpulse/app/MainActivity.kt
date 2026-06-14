package com.fitpulse.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.fitpulse.app.data.AppDatabase
import com.fitpulse.app.data.Exercise
import com.fitpulse.app.navigation.AppNavigation
import com.fitpulse.app.ui.theme.FitPulseTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        seedDatabaseIfEmpty()
        setContent {
            FitPulseApp()
        }
    }

    private fun seedDatabaseIfEmpty() {
        val dao = AppDatabase.getInstance(applicationContext).exerciseDao()
        lifecycleScope.launch {
            if (dao.count() == 0) {
                dao.insert(
                    Exercise(name = "Push-ups", muscleGroup = "Chest", sets = 3, reps = 15)
                )
            }
            Log.d("FitPulse", "Exercises in database: ${dao.count()}")
        }
    }
}

@Composable
fun FitPulseApp() {
    FitPulseTheme {
        AppNavigation()
    }
}

@Preview(showBackground = true)
@Composable
fun FitPulseAppPreview() {
    FitPulseApp()
}

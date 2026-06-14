package com.fitpulse.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fitpulse.app.data.Exercise

@Composable
fun ExerciseListScreen(
    exercises: List<Exercise>,
    onAddExerciseClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "My Exercises",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAddExerciseClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Exercise")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(exercises) { exercise ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(exercise.name, style = MaterialTheme.typography.titleMedium)
                        Text("Category: ${exercise.category}")
                        Text("Sets: ${exercise.sets} | Reps: ${exercise.reps}")
                        Text("Weight: ${exercise.weight} kg")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
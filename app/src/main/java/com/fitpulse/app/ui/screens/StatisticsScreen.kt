package com.fitpulse.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fitpulse.app.data.Exercise

@Composable
fun StatisticsScreen(
    exercises: List<Exercise>,
    onBackClick: () -> Unit
) {
    val totalExercises = exercises.size
    val totalSets = exercises.sumOf { it.sets }
    val totalReps = exercises.sumOf { it.reps }
    val averageWeight = if (exercises.isNotEmpty()) {
        exercises.map { it.weight }.average()
    } else {
        0.0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Statistics",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Total exercises: $totalExercises")
        Text("Total sets: $totalSets")
        Text("Total reps: $totalReps")
        Text("Average weight: ${"%.1f".format(averageWeight)} kg")

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
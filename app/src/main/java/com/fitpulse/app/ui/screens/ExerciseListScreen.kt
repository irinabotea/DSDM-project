package com.fitpulse.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fitpulse.app.data.Exercise
import com.fitpulse.app.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseListScreen(
    exercises: List<Exercise>,
    modifier: Modifier = Modifier,
    onAddExercise: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("My Exercises") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddExercise) {
                Icon(Icons.Filled.Add, contentDescription = "Add exercise")
            }
        }
    ) { innerPadding ->
        if (exercises.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No exercises yet. Tap + to add one.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            val grouped = exercises.groupBy { DateUtils.startOfDay(it.date) }
                .toSortedMap(compareByDescending { it })
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                grouped.forEach { (dayStart, dayExercises) ->
                    item(key = "header-$dayStart") {
                        Text(
                            text = DateUtils.dayLabel(dayStart),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    items(dayExercises, key = { it.id }) { exercise ->
                        ExerciseItem(exercise)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExerciseItem(exercise: Exercise) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = exercise.muscleGroup,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${exercise.sets} sets x ${exercise.reps} reps",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

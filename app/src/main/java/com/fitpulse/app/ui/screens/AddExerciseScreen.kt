package com.fitpulse.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fitpulse.app.data.Exercise

@Composable
fun AddExerciseScreen(
    onSaveExercise: (Exercise) -> Unit,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Add Exercise",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Exercise name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = sets, onValueChange = { sets = it }, label = { Text("Sets") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = reps, onValueChange = { reps = it }, label = { Text("Reps") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val exercise = Exercise(
                    id = System.currentTimeMillis().toInt(),
                    name = name,
                    category = category,
                    sets = sets.toIntOrNull() ?: 0,
                    reps = reps.toIntOrNull() ?: 0,
                    weight = weight.toDoubleOrNull() ?: 0.0
                )
                onSaveExercise(exercise)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Exercise")
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
package com.fitpulse.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fitpulse.app.data.PredefinedExercises
import com.fitpulse.app.ui.theme.FitPulseTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddExerciseScreen(
    modifier: Modifier = Modifier,
    onSave: (name: String, muscleGroup: String, sets: Int, reps: Int) -> Unit = { _, _, _, _ -> }
) {
    var name by remember { mutableStateOf("") }
    var muscleGroup by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var selectedPreset by remember { mutableStateOf<String?>(null) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var muscleError by remember { mutableStateOf<String?>(null) }
    var setsError by remember { mutableStateOf<String?>(null) }
    var repsError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Add exercise",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Choose from common exercises",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PredefinedExercises.all.forEach { preset ->
                FilterChip(
                    selected = selectedPreset == preset.name,
                    onClick = {
                        selectedPreset = preset.name
                        name = preset.name
                        muscleGroup = preset.muscleGroup
                        nameError = null
                        muscleError = null
                    },
                    label = { Text(preset.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Or add your own",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = null
                selectedPreset = null
            },
            label = { Text("Name") },
            singleLine = true,
            isError = nameError != null,
            supportingText = { nameError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = muscleGroup,
            onValueChange = {
                muscleGroup = it
                muscleError = null
                selectedPreset = null
            },
            label = { Text("Muscle group") },
            singleLine = true,
            isError = muscleError != null,
            supportingText = { muscleError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = sets,
            onValueChange = { sets = it; setsError = null },
            label = { Text("Sets") },
            singleLine = true,
            isError = setsError != null,
            supportingText = { setsError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = reps,
            onValueChange = { reps = it; repsError = null },
            label = { Text("Reps") },
            singleLine = true,
            isError = repsError != null,
            supportingText = { repsError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val setsValue = sets.toIntOrNull()
                val repsValue = reps.toIntOrNull()

                nameError = if (name.isBlank()) "Name is required" else null
                muscleError = if (muscleGroup.isBlank()) "Muscle group is required" else null
                setsError = if (setsValue == null || setsValue <= 0) {
                    "Enter a valid number of sets"
                } else null
                repsError = if (repsValue == null || repsValue <= 0) {
                    "Enter a valid number of reps"
                } else null

                if (nameError == null && muscleError == null &&
                    setsError == null && repsError == null
                ) {
                    onSave(name.trim(), muscleGroup.trim(), setsValue!!, repsValue!!)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun AddExerciseScreenPreview() {
    FitPulseTheme {
        AddExerciseScreen()
    }
}

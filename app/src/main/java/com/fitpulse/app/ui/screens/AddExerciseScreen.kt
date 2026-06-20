package com.fitpulse.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fitpulse.app.data.PredefinedExercises
import com.fitpulse.app.data.TrackingType
import com.fitpulse.app.ui.components.SearchableDropdownField
import com.fitpulse.app.ui.theme.FitPulseTheme
import com.fitpulse.app.ui.viewmodel.MuscleGroupUiState
import com.fitpulse.app.util.DateUtils

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseScreen(
    modifier: Modifier = Modifier,
    muscleGroupState: MuscleGroupUiState = MuscleGroupUiState(),
    onBack: () -> Unit = {},
    onRetryMuscleGroups: () -> Unit = {},
    onSave: (
        name: String,
        muscleGroup: String,
        category: String,
        trackingType: String,
        sets: Int,
        reps: Int,
        durationMinutes: Int,
        date: Long
    ) -> Unit = { _, _, _, _, _, _, _, _ -> }
) {
    var name by remember { mutableStateOf("") }
    var muscleGroup by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var trackingType by remember { mutableStateOf(TrackingType.REPS) }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }
    var selectedPreset by remember { mutableStateOf<String?>(null) }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var muscleError by remember { mutableStateOf<String?>(null) }
    var categoryError by remember { mutableStateOf<String?>(null) }
    var setsError by remember { mutableStateOf<String?>(null) }
    var repsError by remember { mutableStateOf<String?>(null) }
    var durationError by remember { mutableStateOf<String?>(null) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selectedDate = it }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Add exercise") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
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
                        category = preset.category
                        trackingType = preset.trackingType
                        nameError = null
                        muscleError = null
                        categoryError = null
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

        when {
            muscleGroupState.isLoading -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Loading categories & muscles…",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            muscleGroupState.error != null -> {
                Text(
                    text = muscleGroupState.error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
                TextButton(onClick = onRetryMuscleGroups) { Text("Retry") }
            }
        }

        SearchableDropdownField(
            value = category,
            onValueChange = { category = it; categoryError = null },
            label = "Category",
            options = muscleGroupState.categories,
            isError = categoryError != null,
            supportingText = categoryError
        )
        Spacer(modifier = Modifier.height(12.dp))

        SearchableDropdownField(
            value = muscleGroup,
            onValueChange = { muscleGroup = it; muscleError = null },
            label = "Muscle group",
            options = muscleGroupState.muscles,
            isError = muscleError != null,
            supportingText = muscleError
        )
        Spacer(modifier = Modifier.height(12.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text("Tracking", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            val options = listOf(TrackingType.REPS to "Reps", TrackingType.TIME to "Time")
            options.forEachIndexed { index, (type, label) ->
                SegmentedButton(
                    selected = trackingType == type,
                    onClick = { trackingType = type },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)
                ) {
                    Text(label)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        if (trackingType == TrackingType.REPS) {
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
        } else {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = hours,
                    onValueChange = { hours = it.filter { c -> c.isDigit() }; durationError = null },
                    label = { Text("Hours") },
                    singleLine = true,
                    isError = durationError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedTextField(
                    value = minutes,
                    onValueChange = { minutes = it.filter { c -> c.isDigit() }; durationError = null },
                    label = { Text("Minutes") },
                    singleLine = true,
                    isError = durationError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            durationError?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Date", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(DateUtils.dayLabel(selectedDate))
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                nameError = if (name.isBlank()) "Name is required" else null
                muscleError = if (muscleGroup.isBlank()) "Muscle group is required" else null
                categoryError = if (category.isBlank()) "Category is required" else null

                val commonValid = nameError == null && muscleError == null && categoryError == null

                if (trackingType == TrackingType.REPS) {
                    val setsValue = sets.toIntOrNull()
                    val repsValue = reps.toIntOrNull()
                    setsError = if (setsValue == null || setsValue <= 0) {
                        "Enter a valid number of sets"
                    } else null
                    repsError = if (repsValue == null || repsValue <= 0) {
                        "Enter a valid number of reps"
                    } else null

                    if (commonValid && setsError == null && repsError == null &&
                        setsValue != null && repsValue != null
                    ) {
                        onSave(
                            name.trim(), muscleGroup.trim(), category.trim(),
                            TrackingType.REPS.name, setsValue, repsValue, 0, selectedDate
                        )
                    }
                } else {
                    val totalMinutes = (hours.toIntOrNull() ?: 0) * 60 + (minutes.toIntOrNull() ?: 0)
                    durationError = if (totalMinutes <= 0) {
                        "Enter a duration of at least 1 minute"
                    } else null

                    if (commonValid && durationError == null) {
                        onSave(
                            name.trim(), muscleGroup.trim(), category.trim(),
                            TrackingType.TIME.name, 0, 0, totalMinutes, selectedDate
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
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
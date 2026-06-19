package com.fitpulse.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fitpulse.app.data.Exercise
import com.fitpulse.app.util.DateUtils
import com.fitpulse.app.util.StatsPeriod
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(exercises: List<Exercise>) {
    var period by remember { mutableStateOf(StatsPeriod.TOTAL) }
    var anchor by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val range = DateUtils.periodRange(period, anchor)
    val filtered = exercises.filter { it.date >= range.start && it.date < range.endExclusive }

    val totalExercises = filtered.size
    val totalSets = filtered.sumOf { it.sets }
    val totalReps = filtered.sumOf { it.sets * it.reps }

    val topMuscleGroup = filtered
        .groupBy { it.muscleGroup }
        .maxByOrNull { entry -> entry.value.sumOf { it.sets * it.reps } }
        ?.key ?: "No data"

    val topExercise = filtered.maxByOrNull { it.sets * it.reps }

    val averageSets =
        if (totalExercises > 0) totalSets.toDouble() / totalExercises else 0.0

    val averageReps =
        if (totalExercises > 0) totalReps.toDouble() / totalExercises else 0.0

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = anchor)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { anchor = it }
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Workout Statistics",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                StatsPeriod.entries.forEachIndexed { index, p ->
                    SegmentedButton(
                        selected = period == p,
                        onClick = { period = p },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = StatsPeriod.entries.size
                        )
                    ) {
                        Text(p.label)
                    }
                }
            }
        }

        if (period != StatsPeriod.TOTAL) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { anchor = DateUtils.stepAnchor(period, anchor, -1) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous period"
                        )
                    }
                    TextButton(onClick = { showDatePicker = true }) {
                        Text(
                            text = range.label,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = { anchor = DateUtils.stepAnchor(period, anchor, 1) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next period"
                        )
                    }
                }
            }
        }

        item { StatisticCard("Total exercises", totalExercises.toString()) }
        item { StatisticCard("Total sets", totalSets.toString()) }
        item { StatisticCard("Total repetitions", totalReps.toString()) }

        item { MuscleGroupBarChart(exercises = filtered) }

        item { MuscleGroupDonutChart(exercises = filtered) }

        item { StatisticCard("Most trained muscle group", topMuscleGroup) }

        item {
            StatisticCard(
                "Average sets / exercise",
                String.format(Locale.getDefault(), "%.1f", averageSets)
            )
        }

        item {
            StatisticCard(
                "Average reps volume / exercise",
                String.format(Locale.getDefault(), "%.1f", averageReps)
            )
        }

        item {
            StatisticCard(
                "Top exercise",
                topExercise?.let { "${it.name} (${it.sets * it.reps} reps)" } ?: "No data"
            )
        }

        item { MuscleGroupSection(exercises = filtered) }
    }
}

@Composable
fun StatisticCard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MuscleGroupBarChart(exercises: List<Exercise>) {
    val data = exercises
        .groupBy { it.muscleGroup }
        .mapValues { entry -> entry.value.sumOf { it.sets * it.reps } }
        .filter { it.value > 0 }
        .toList()
        .sortedByDescending { it.second }

    if (data.isEmpty()) {
        StatisticCard("Muscle Group Chart", "No chart data available yet")
        return
    }

    val maxValue = data.maxOf { it.second }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Muscle Group Chart",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            data.forEach { (group, value) ->
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(group, fontWeight = FontWeight.SemiBold)
                        Text("$value reps")
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .background(
                                color = Color(0xFFE8EDF3),
                                shape = RoundedCornerShape(50)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(value.toFloat() / maxValue.toFloat())
                                .height(16.dp)
                                .background(
                                    color = Color(0xFF7CA7D8),
                                    shape = RoundedCornerShape(50)
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MuscleGroupDonutChart(exercises: List<Exercise>) {
    val data = exercises
        .groupBy { it.muscleGroup }
        .mapValues { entry -> entry.value.sumOf { it.sets * it.reps } }
        .filter { it.value > 0 }

    val total = data.values.sum()

    if (data.isEmpty() || total == 0) {
        StatisticCard("Workout Distribution", "No workout distribution available yet")
        return
    }

    val colors = listOf(
        Color(0xFF7CA7D8),
        Color(0xFFA8D5BA),
        Color(0xFFFFC8A2),
        Color(0xFFD8B4E2),
        Color(0xFFFFA6A6),
        Color(0xFFB8C0FF)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Workout Distribution",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                var startAngle = -90f

                data.entries.forEachIndexed { index, entry ->
                    val sweepAngle =
                        entry.value.toFloat() / total.toFloat() * 360f

                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = 46f)
                    )

                    startAngle += sweepAngle
                }
            }

            data.entries.forEachIndexed { index, entry ->
                val percent =
                    ((entry.value.toFloat() / total.toFloat()) * 100).roundToInt()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(
                                    color = colors[index % colors.size],
                                    shape = RoundedCornerShape(50)
                                )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(entry.key)
                    }

                    Text("$percent%")
                }
            }
        }
    }
}

@Composable
fun MuscleGroupSection(exercises: List<Exercise>) {
    val totalVolume = exercises.sumOf { it.sets * it.reps }

    val groups = exercises
        .groupBy { it.muscleGroup }
        .mapValues { entry -> entry.value.sumOf { it.sets * it.reps } }
        .toList()
        .sortedByDescending { it.second }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Volume by muscle group",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (groups.isEmpty()) {
                Text("No exercises added yet.")
            } else {
                groups.forEach { (group, volume) ->
                    val percent =
                        if (totalVolume > 0) {
                            ((volume.toFloat() / totalVolume.toFloat()) * 100).roundToInt()
                        } else {
                            0
                        }

                    Text("$group: $volume reps • $percent%")
                }
            }
        }
    }
}
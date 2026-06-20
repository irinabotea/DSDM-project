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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.fitpulse.app.data.UserProfile
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    username: String,
    profile: UserProfile,
    modifier: Modifier = Modifier,
    onSaveProfile: (UserProfile) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var weight by remember { mutableStateOf(profile.weightKg) }
    var height by remember { mutableStateOf(profile.heightCm) }
    var age by remember { mutableStateOf(profile.age) }
    var gender by remember { mutableStateOf(profile.gender) }
    var goal by remember { mutableStateOf(profile.goal) }
    var saved by remember { mutableStateOf(false) }

    val currentProfile = UserProfile(weight, height, age, gender, goal)
    val bmi = currentProfile.bmi()

    val weightError = UserProfile.weightError(weight)
    val heightError = UserProfile.heightError(height)
    val ageError = UserProfile.ageError(age)
    val hasErrors = weightError != null || heightError != null || ageError != null

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (username.isNotBlank()) "Logged in as $username" else "Logged in",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text("Body metrics", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = filterDecimal(it); saved = false },
            label = { Text("Weight (kg)") },
            singleLine = true,
            isError = weightError != null,
            supportingText = { weightError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = height,
            onValueChange = { height = filterDecimal(it); saved = false },
            label = { Text("Height (cm)") },
            singleLine = true,
            isError = heightError != null,
            supportingText = { heightError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = filterInteger(it); saved = false },
            label = { Text("Age") },
            singleLine = true,
            isError = ageError != null,
            supportingText = { ageError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Gender", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            UserProfile.genders.forEach { option ->
                FilterChip(
                    selected = gender == option,
                    onClick = { gender = option; saved = false },
                    label = { Text(option) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Fitness goal", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            UserProfile.goals.forEach { option ->
                FilterChip(
                    selected = goal == option,
                    onClick = { goal = option; saved = false },
                    label = { Text(option) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (bmi != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("BMI", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = String.format(Locale.getDefault(), "%.1f", bmi) +
                            " · " + UserProfile.bmiCategory(bmi),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Text(
                text = "Enter your weight and height to see your BMI.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        val calorieAdvice = if (hasErrors) null else currentProfile.calorieAdvice()
        if (calorieAdvice != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Daily calorie goal", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${calorieAdvice.target} kcal / day",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Maintenance: ${calorieAdvice.maintenance} kcal · Goal: $goal",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = calorieAdvice.tip,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Text(
                text = "Fill in weight, height, age, gender and goal to get a daily calorie suggestion.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                onSaveProfile(currentProfile)
                saved = true
            },
            enabled = !hasErrors,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (saved) "Saved" else "Save profile")
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}

/** Keeps only digits and a single decimal separator. */
private fun filterDecimal(input: String): String {
    val cleaned = input.replace(',', '.').filter { it.isDigit() || it == '.' }
    val firstDot = cleaned.indexOf('.')
    if (firstDot == -1) return cleaned
    val intPart = cleaned.substring(0, firstDot + 1)
    val decimals = cleaned.substring(firstDot + 1).replace(".", "")
    return intPart + decimals
}

/** Keeps only digits. */
private fun filterInteger(input: String): String = input.filter { it.isDigit() }

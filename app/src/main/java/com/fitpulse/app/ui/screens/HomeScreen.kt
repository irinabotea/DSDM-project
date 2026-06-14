package com.fitpulse.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitpulse.app.ui.theme.FitPulseTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onMyExercises: () -> Unit = {},
    onStatistics: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "FitPulse",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Monitorizează-ți activitatea și progresul",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onMyExercises,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("My Exercises")
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onStatistics,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Statistics")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FitPulseTheme {
        HomeScreen()
    }
}

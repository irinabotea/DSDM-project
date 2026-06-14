package com.fitpulse.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fitpulse.app.navigation.AppNavigation
import com.fitpulse.app.ui.theme.FitPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            FitPulseApp()
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

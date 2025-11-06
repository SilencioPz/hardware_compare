package com.silenciopz.hardware

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.silenciopz.hardware.ui.components.AppScreen
import com.silenciopz.hardware.ui.components.GamePerformanceScreen
import com.silenciopz.hardware.ui.components.MainMenuScreen
import com.silenciopz.hardware.ui.components.*
import com.silenciopz.hardware.ui.theme.SilencioHardwareStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilencioHardwareStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent()
                }
            }
        }
    }
}

@Composable
private fun AppContent() {
    var currentScreen by remember { mutableStateOf(AppScreen.MAIN_MENU) }

    when (currentScreen) {
        AppScreen.MAIN_MENU -> MainMenuScreen(
            onNavigate = { screen -> currentScreen = screen }
        )

        AppScreen.CPU_COMPARISON -> CpuComparisonScreen(
            onBackClick = { currentScreen = AppScreen.MAIN_MENU }
        )

        AppScreen.GPU_COMPARISON -> GpuComparisonScreen(
            onBackClick = { currentScreen = AppScreen.MAIN_MENU }
        )

        AppScreen.GAME_PERFORMANCE -> GamePerformanceScreen(
            onBackClick = { currentScreen = AppScreen.MAIN_MENU }
        )
    }
}
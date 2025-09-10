package com.example.silenciohardwarestore.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silenciohardwarestore.R

enum class AppScreen {
    MAIN_MENU,
    CPU_COMPARISON,
    GPU_COMPARISON,
    GAME_PERFORMANCE
}

@Composable
fun MainMenuScreen(
    onNavigate: (AppScreen) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        HeaderSection(
            title = "Silencio Hardware Store"
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Selecione uma opção:",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MenuButton(
                icon = Icons.Default.Memory,
                title = "Comparar CPUs",
                description = "Compare processadores lado a lado",
                onClick = { onNavigate(AppScreen.CPU_COMPARISON) }
            )

            MenuButton(
                icon = Icons.Default.Memory,
                title = "Comparar GPUs",
                description = "Compare placas de vídeo lado a lado",
                onClick = { onNavigate(AppScreen.GPU_COMPARISON) }
            )

            MenuButton(
                icon = Icons.Default.Games,
                title = "Desempenho em Jogos",
                description = "Veja performance em diferentes jogos",
                onClick = { onNavigate(AppScreen.GAME_PERFORMANCE) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Versão 1.0.0",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MenuButton(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.Default.CompareArrows,
                contentDescription = "Navegar",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun HeaderSection(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.silenciopz_logo),
                contentDescription = "Logo SilencioHardwareStore",
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 12.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                title,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
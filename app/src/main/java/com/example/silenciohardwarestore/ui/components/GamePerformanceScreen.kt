package com.example.silenciohardwarestore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Games
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silenciohardwarestore.data.GameDataSource
import com.example.silenciohardwarestore.data.CpuDataSource
import com.example.silenciohardwarestore.data.GpuDataSource
import com.example.silenciohardwarestore.data.Cpu
import com.example.silenciohardwarestore.data.Gpu
import com.example.silenciohardwarestore.data.Game
import com.example.silenciohardwarestore.utils.BenchmarkGames
import kotlin.math.abs

@Composable
fun GamePerformanceScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedCpu by remember { mutableStateOf("") }
    var selectedGpu by remember { mutableStateOf("") }
    var selectedGame by remember { mutableStateOf("") }
    var selectedResolution by remember { mutableStateOf("1920x1080") }
    var showResult by remember { mutableStateOf(false) }

    val cpus = remember { CpuDataSource.loadCpus().map { it.name } }
    val gpus = remember { GpuDataSource.loadGpus().map { it.name } }
    val games = remember { GameDataSource.loadGames() }

    val selectedCpuObj = CpuDataSource.loadCpus().find { it.name == selectedCpu }
    val selectedGpuObj = GpuDataSource.loadGpus().find { it.name == selectedGpu }
    val selectedGameObj = GameDataSource.loadGames().find { it.name == selectedGame }
    val resolutions = listOf("1024x768", "1920x1080", "2560x1440", "3840x2160")

    fun clearAll() {
        selectedCpu = ""
        selectedGpu = ""
        selectedGame = ""
        selectedResolution = "1920x1080"
        showResult = false
    }

    val completePerformanceData = remember(selectedCpu, selectedGpu, selectedGame, selectedResolution) {
        if (selectedCpuObj != null && selectedGpuObj != null && selectedGameObj != null) {
            val benchmark = BenchmarkGames()
            benchmark.calculateGamePerformance(selectedCpuObj, selectedGpuObj, selectedGameObj, selectedResolution)
        } else {
            null
        }
    }

    val hardwareAnalysis = remember(selectedCpuObj, selectedGpuObj, selectedGameObj, selectedResolution) {
        if (selectedCpuObj != null && selectedGpuObj != null && selectedGameObj != null) {
            analyzeHardwareAdequacy(selectedCpuObj, selectedGpuObj, selectedGameObj, selectedResolution)
        } else {
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                ),
                modifier = Modifier.height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Voltar", fontSize = 14.sp)
            }

            Button(
                onClick = { clearAll() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                modifier = Modifier.height(48.dp),
                enabled = selectedCpu.isNotEmpty() || selectedGpu.isNotEmpty() || selectedGame.isNotEmpty() || selectedResolution != "1920x1080"
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Limpar",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Limpar", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "🎮 Cálculo de Bottleneck",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(
                    "Selecione seu hardware e veja o desempenho em jogos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                DropdownMenuField(
                    label = "Processador (CPU)",
                    options = cpus,
                    selectedOption = selectedCpu,
                    onOptionSelected = { selectedCpu = it }
                )
            }

            item {
                DropdownMenuField(
                    label = "Placa de Vídeo (GPU)",
                    options = gpus,
                    selectedOption = selectedGpu,
                    onOptionSelected = { selectedGpu = it }
                )
            }

            item {
                DropdownMenuField(
                    label = "Jogo",
                    options = games.map { it.name },
                    selectedOption = selectedGame,
                    onOptionSelected = { selectedGame = it }
                )
            }

            item {
                DropdownMenuField(
                    label = "Resolução",
                    options = resolutions,
                    selectedOption = selectedResolution,
                    onOptionSelected = { selectedResolution = it }
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { clearAll() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        enabled = selectedCpu.isNotEmpty() || selectedGpu.isNotEmpty() || selectedGame.isNotEmpty() || selectedResolution != "1920x1080"
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpar",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Limpar")
                    }

                    Button(
                        onClick = { showResult = true },
                        enabled = selectedCpu.isNotEmpty() && selectedGpu.isNotEmpty() && selectedGame.isNotEmpty(),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Games,
                            contentDescription = "Calcular",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Calcular")
                    }
                }
            }

            if (showResult && completePerformanceData != null && selectedGameObj != null && hardwareAnalysis != null) {
                item {
                    HardwareAnalysisCard(hardwareAnalysis = hardwareAnalysis)
                }

                item {
                    CompleteGamePerformanceResultCard(
                        performanceData = completePerformanceData,
                        gameName = selectedGameObj.name,
                        cpuName = selectedCpu,
                        gpuName = selectedGpu,
                        resolution = selectedResolution
                    )
                }
            }
        }
    }
}

fun analyzeHardwareAdequacy(cpu: Cpu, gpu: Gpu, game: Game, resolution: String): HardwareAnalysis {
    val benchmark = BenchmarkGames()
    val cpuScore = benchmark.calculateCpuGamingScore(cpu).toFloat()
    val gpuScore = benchmark.calculateGpuGamingScore(gpu).toFloat()

    val gameRequirements = getGameRequirements(game, resolution)

    val cpuAdequacy = when {
        cpuScore >= gameRequirements.recommendedCpu * 1.5f -> ComponentStatus.OVERKILL
        cpuScore >= gameRequirements.recommendedCpu * 1.2f -> ComponentStatus.EXCELLENT
        cpuScore >= gameRequirements.recommendedCpu -> ComponentStatus.ADEQUATE
        cpuScore >= gameRequirements.minimumCpu * 1.2f -> ComponentStatus.ADEQUATE
        else -> ComponentStatus.INSUFFICIENT
    }

    val gpuAdequacy = when {
        gpuScore >= gameRequirements.recommendedGpu * 1.5f -> ComponentStatus.OVERKILL
        gpuScore >= gameRequirements.recommendedGpu * 1.2f -> ComponentStatus.EXCELLENT
        gpuScore >= gameRequirements.recommendedGpu -> ComponentStatus.ADEQUATE
        gpuScore >= gameRequirements.minimumGpu * 1.2f -> ComponentStatus.ADEQUATE
        else -> ComponentStatus.INSUFFICIENT
    }

    val overallStatus = determineOverallStatus(cpuAdequacy, gpuAdequacy, cpuScore, gpuScore, gameRequirements)

    return HardwareAnalysis(
        overallStatus = overallStatus,
        cpuStatus = cpuAdequacy,
        gpuStatus = gpuAdequacy,
        message = generateAnalysisMessage(overallStatus, cpuAdequacy, gpuAdequacy),
        recommendation = generateRecommendation(overallStatus, cpuAdequacy, gpuAdequacy, game.name),
        confidenceLevel = calculateConfidence(cpuScore, gpuScore, gameRequirements)
    )
}

fun getGameRequirements(game: Game, resolution: String): GameRequirements {
    val resolutionMultiplier = when (resolution) {
        "1024x768" -> 0.6f
        "1920x1080" -> 1.0f
        "2560x1440" -> 1.6f
        "3840x2160" -> 2.8f
        else -> 1.0f
    }

    val baseMinCpu = game.getMinCpuScore().toFloat()
    val baseRecCpu = game.getRecCpuScore().toFloat()
    val baseMinGpu = game.getMinGpuScore().toFloat()
    val baseRecGpu = game.getRecGpuScore().toFloat()

    return GameRequirements(
        minimumCpu = baseMinCpu * resolutionMultiplier,
        recommendedCpu = baseRecCpu * resolutionMultiplier,
        minimumGpu = baseMinGpu * resolutionMultiplier,
        recommendedGpu = baseRecGpu * resolutionMultiplier
    )
}

fun determineOverallStatus(
    cpuStatus: ComponentStatus,
    gpuStatus: ComponentStatus,
    cpuScore: Float,
    gpuScore: Float,
    requirements: GameRequirements
): OverallStatus {
    return when {
        cpuStatus == ComponentStatus.INSUFFICIENT && gpuStatus == ComponentStatus.INSUFFICIENT ->
            OverallStatus.INSUFFICIENT_BOTH

        cpuStatus == ComponentStatus.INSUFFICIENT -> OverallStatus.CPU_BOTTLENECK
        gpuStatus == ComponentStatus.INSUFFICIENT -> OverallStatus.GPU_BOTTLENECK

        (cpuStatus == ComponentStatus.OVERKILL || cpuStatus == ComponentStatus.EXCELLENT) &&
                (gpuStatus == ComponentStatus.OVERKILL || gpuStatus == ComponentStatus.EXCELLENT) -> {
            if (cpuScore > requirements.recommendedCpu * 1.5f && gpuScore > requirements.recommendedGpu * 1.5f)
                OverallStatus.OVERKILL
            else OverallStatus.EXCELLENT
        }

        cpuStatus == ComponentStatus.ADEQUATE && gpuStatus == ComponentStatus.ADEQUATE ->
            OverallStatus.ADEQUATE

        cpuStatus == ComponentStatus.EXCELLENT && gpuStatus == ComponentStatus.ADEQUATE ->
            OverallStatus.CPU_STRONG_GPU_OK

        cpuStatus == ComponentStatus.ADEQUATE && gpuStatus == ComponentStatus.EXCELLENT ->
            OverallStatus.GPU_STRONG_CPU_OK

        else -> OverallStatus.BALANCED
    }
}

fun generateAnalysisMessage(
    overall: OverallStatus,
    cpuStatus: ComponentStatus,
    gpuStatus: ComponentStatus
): String {
    return when (overall) {
        OverallStatus.OVERKILL -> "🚀 Hardware mais que suficiente! Configuração premium."
        OverallStatus.EXCELLENT -> "✅ Hardware excelente! Ideal para este jogo."
        OverallStatus.BALANCED -> "⚖️ Hardware balanceado e adequado."
        OverallStatus.ADEQUATE -> "✔️ Hardware suficiente, mas pode ter limitações."
        OverallStatus.CPU_STRONG_GPU_OK -> "💪 CPU excelente, GPU adequada."
        OverallStatus.GPU_STRONG_CPU_OK -> "🎨 GPU excelente, CPU adequada."
        OverallStatus.CPU_BOTTLENECK -> "⚠️ CPU pode limitar o desempenho."
        OverallStatus.GPU_BOTTLENECK -> "⚠️ GPU pode limitar o desempenho."
        OverallStatus.INSUFFICIENT_BOTH -> "❌ Hardware insuficiente para este jogo."
    }
}

fun generateRecommendation(
    overall: OverallStatus,
    cpuStatus: ComponentStatus,
    gpuStatus: ComponentStatus,
    gameName: String
): String {
    return when (overall) {
        OverallStatus.OVERKILL -> "Use configurações Ultra/Máximas com tranquilidade."
        OverallStatus.EXCELLENT -> "Configurações Altas/Ultra recomendadas."
        OverallStatus.BALANCED -> "Configurações Médias/Altas para melhor equilíbrio."
        OverallStatus.ADEQUATE -> "Use configurações Médias para performance estável."
        OverallStatus.CPU_STRONG_GPU_OK -> "Considere upgrade da GPU para melhor experiência."
        OverallStatus.GPU_STRONG_CPU_OK -> "Upgrade do CPU melhoraria o desempenho."
        OverallStatus.CPU_BOTTLENECK -> "CPU é o principal limitante - considere upgrade."
        OverallStatus.GPU_BOTTLENECK -> "GPU é o principal limitante - considere upgrade."
        OverallStatus.INSUFFICIENT_BOTH -> "Configurações Baixas ou upgrade do sistema."
    }
}

fun calculateConfidence(cpuScore: Float, gpuScore: Float, requirements: GameRequirements): Float {
    val cpuRatio = cpuScore / requirements.recommendedCpu
    val gpuRatio = gpuScore / requirements.recommendedGpu
    val averageRatio = (cpuRatio + gpuRatio) / 2f

    return when {
        averageRatio >= 1.5f -> 0.95f
        averageRatio >= 1.2f -> 0.90f
        averageRatio >= 1.0f -> 0.85f
        averageRatio >= 0.8f -> 0.75f
        averageRatio >= 0.6f -> 0.65f
        else -> 0.50f
    }
}

@Composable
fun HardwareAnalysisCard(hardwareAnalysis: HardwareAnalysis) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (hardwareAnalysis.overallStatus) {
                OverallStatus.OVERKILL -> Color(0xFF0D4F3B)
                OverallStatus.EXCELLENT -> Color(0xFF1B4332)
                OverallStatus.BALANCED, OverallStatus.ADEQUATE -> Color(0xFF2D3A1F)
                OverallStatus.CPU_STRONG_GPU_OK, OverallStatus.GPU_STRONG_CPU_OK -> Color(0xFF3D2914)
                else -> Color(0xFF4A1C1C)
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🔍 Análise Inteligente",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = hardwareAnalysis.message,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ComponentStatusChip(
                    label = "CPU",
                    status = hardwareAnalysis.cpuStatus,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                ComponentStatusChip(
                    label = "GPU",
                    status = hardwareAnalysis.gpuStatus,
                    modifier = Modifier.weight(1f)
                )
            }

            if (hardwareAnalysis.recommendation.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "💡 Recomendação",
                            color = Color.Cyan,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = hardwareAnalysis.recommendation,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Confiança da análise:",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
                Text(
                    text = "${(hardwareAnalysis.confidenceLevel * 100).toInt()}%",
                    color = Color.Cyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ComponentStatusChip(
    label: String,
    status: ComponentStatus,
    modifier: Modifier = Modifier
) {
    val (color, icon, text) = when (status) {
        ComponentStatus.OVERKILL -> Triple(Color.Green, "🚀", "Overkill")
        ComponentStatus.EXCELLENT -> Triple(Color.Green, "✅", "Excelente")
        ComponentStatus.ADEQUATE -> Triple(Color.Yellow, "✔️", "Adequado")
        ComponentStatus.INSUFFICIENT -> Triple(Color.Red, "⚠️", "Insuficiente")
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$icon $label",
                color = color,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = text,
                color = color,
                fontSize = 12.sp
            )
        }
    }
}

data class HardwareAnalysis(
    val overallStatus: OverallStatus,
    val cpuStatus: ComponentStatus,
    val gpuStatus: ComponentStatus,
    val message: String,
    val recommendation: String,
    val confidenceLevel: Float
)

data class GameRequirements(
    val minimumCpu: Float,
    val recommendedCpu: Float,
    val minimumGpu: Float,
    val recommendedGpu: Float
)

enum class ComponentStatus {
    OVERKILL, EXCELLENT, ADEQUATE, INSUFFICIENT
}

enum class OverallStatus {
    OVERKILL, EXCELLENT, BALANCED, ADEQUATE,
    CPU_STRONG_GPU_OK, GPU_STRONG_CPU_OK,
    CPU_BOTTLENECK, GPU_BOTTLENECK, INSUFFICIENT_BOTH
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.DarkGray,
                    focusedContainerColor = Color.DarkGray,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Blue
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.Black)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                option,
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        modifier = Modifier.background(Color.Black)
                    )
                }
            }
        }
    }
}

@Composable
fun CompleteGamePerformanceResultCard(
    performanceData: Map<String, List<String>>,
    gameName: String,
    cpuName: String,
    gpuName: String,
    resolution: String
) {

    val isOverkill = performanceData["⚡ Bottleneck"]?.getOrNull(1) == "0.0%" &&
            performanceData["⚡ Bottleneck"]?.getOrNull(2) == "Hardware Overkill"

    val bottleneckPercentage = performanceData["⚡ Bottleneck"]
        ?.getOrNull(1)?.replace("%", "")?.toFloatOrNull() ?: 0f
    val bottleneckSeverity = performanceData["⚡ Bottleneck"]?.getOrNull(2) ?: ""
    val bottleneckType = performanceData["⚡ Bottleneck"]?.getOrNull(3) ?: ""

    val isInsufficient = bottleneckPercentage > 20f ||
            bottleneckSeverity.contains("Severo", ignoreCase = true) ||
            bottleneckSeverity.contains("Crítico", ignoreCase = true) ||
            bottleneckType.contains("Insufficient", ignoreCase = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "🎮 $gameName - Análise Completa",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    "💻 $cpuName • 🎨 $gpuName • 📺 $resolution",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
                if (isOverkill) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "✅ Este hardware é excessivamente potente para este jogo",
                        color = Color.Green,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Rodará perfeitamente em qualquer configuração",
                        color = Color.Yellow,
                        fontSize = 12.sp
                    )
                }
                if (isInsufficient) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "⚠️ Hardware pode ser insuficiente para este jogo",
                        color = Color.Red,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Considere reduzir configurações gráficas ou atualizar componentes",
                        color = Color.Yellow,
                        fontSize = 12.sp
                    )
                }
            }
        }

        PerformanceCategoryCard(
            title = "⚡ Performance Principal",
            items = performanceData.filterKeys {
                it.contains("Desempenho no Jogo") || it.contains("Resolução Ideal")
            }
        )

        PerformanceCategoryCard(
            title = "🔧 Análise de Bottleneck",
            items = performanceData.filterKeys {
                it.contains("Bottleneck") || it.contains("Configuração Recomendada")
            }
        )

        PerformanceCategoryCard(
            title = "🌡️ Temperatura & Estabilidade",
            items = performanceData.filterKeys {
                it.contains("Temperatura") || it.contains("Risco Térmico")
            }
        )
    }
}

@Composable
fun PerformanceCategoryCard(
    title: String,
    items: Map<String, List<String>>
) {
    if (items.isEmpty()) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                title,
                color = Color.Cyan,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            items.forEach { (category, data) ->
                PerformanceRow(
                    title = category,
                    value1 = data.getOrNull(1) ?: "",
                    value2 = data.getOrNull(2) ?: "",
                    comparison = data.getOrNull(3) ?: ""
                )
                if (items.keys.last() != category) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = Color.Gray.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
fun PerformanceRow(title: String, value1: String, value2: String, comparison: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            color = Color.LightGray,
            fontSize = 14.sp,
            modifier = Modifier.weight(2.5f),
            maxLines = 2
        )

        Text(
            value1,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1.8f),
            textAlign = TextAlign.End
        )

        Text(
            value2,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1.8f),
            textAlign = TextAlign.End
        )

        Text(
            comparison,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.5f),
            textAlign = TextAlign.End,
            color = when {
                comparison.contains("Excelente", ignoreCase = true) -> Color.Green
                comparison.contains("Bom", ignoreCase = true) -> Color.Yellow
                comparison.contains("Adequado", ignoreCase = true) -> Color.Cyan
                comparison.contains("Insuficiente", ignoreCase = true) -> Color.Red
                comparison.contains("Crítico", ignoreCase = true) -> Color.Red
                comparison.contains("Severo", ignoreCase = true) -> Color.Red
                else -> Color.White
            }
        )
    }
}

fun Game.getMinCpuScore(): Int {
    return (15000 * bottleneckMultiplier).toInt()
}

fun Game.getRecCpuScore(): Int {
    return (25000 * bottleneckMultiplier).toInt()
}

fun Game.getMinGpuScore(): Int {
    return (8000 * bottleneckMultiplier).toInt()
}

fun Game.getRecGpuScore(): Int {
    return (15000 * bottleneckMultiplier).toInt()
}
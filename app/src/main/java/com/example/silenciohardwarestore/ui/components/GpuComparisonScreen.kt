package com.example.silenciohardwarestore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silenciohardwarestore.data.Gpu
import com.example.silenciohardwarestore.data.GpuDataSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.silenciohardwarestore.utils.BenchmarkGpus
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import kotlin.collections.plus

@Composable
fun GpuComparisonScreen(
    onBackClick: () -> Unit = {}
) {
    val gpus = remember { GpuDataSource.loadGpus().filter { it.name.isNotEmpty() } }
    var selectedGpus by remember { mutableStateOf<List<Gpu>>(emptyList()) }
    val benchmarkGpus = remember { BenchmarkGpus() }
    var showComparison by remember { mutableStateOf(false) }
    var viewMode by remember { mutableStateOf(ViewMode.GRID) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredGpus = remember(gpus, searchQuery) {
        if (searchQuery.isBlank()) {
            gpus
        } else {
            gpus.filter { gpu ->
                gpu.name.contains(searchQuery, ignoreCase = true) ||
                        gpu.brand.contains(searchQuery, ignoreCase = true) ||
                        gpu.memory.toString().contains(searchQuery) ||
                        gpu.tdp.toString().contains(searchQuery)
            }
        }
    }

    if (showComparison && selectedGpus.size == 2) {
        ComparisonFullScreen1(
            gpu1 = selectedGpus[0],
            gpu2 = selectedGpus[1],
            benchmarkGpus = benchmarkGpus,
            onBackClick = {
                showComparison = false
                selectedGpus = emptyList()
                searchQuery = ""
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
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

                Text(
                    "Comparar GPUs",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Compare GPUs lado a lado",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            SearchBoxGpu(
                searchQuery = searchQuery,
                onSearchQueryChanged = { newQuery ->
                    searchQuery = newQuery
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SelectionBarGpu(
                selectedCount = selectedGpus.size,
                viewMode = viewMode,
                onViewModeChanged = { viewMode = it },
                onClearSelection = {
                    selectedGpus = emptyList()
                    searchQuery = ""
                },
                searchQuery = searchQuery,
                filteredCount = filteredGpus.size,
                totalCount = gpus.size
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.weight(1f)) {
                when (viewMode) {
                    ViewMode.GRID -> GpuGridView(
                        gpus = filteredGpus,
                        selectedGpus = selectedGpus,
                        onGpuSelected = { gpu, isSelected ->
                            selectedGpus = if (isSelected) {
                                if (selectedGpus.size < 2) {
                                    selectedGpus + gpu
                                } else {
                                    selectedGpus
                                }
                            } else {
                                selectedGpus - gpu
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    ViewMode.LIST -> GpuListView(
                        gpus = filteredGpus,
                        selectedGpus = selectedGpus,
                        onGpuSelected = { gpu, isSelected ->
                            selectedGpus = if (isSelected) {
                                if (selectedGpus.size < 2) selectedGpus + gpu else selectedGpus
                            } else {
                                selectedGpus - gpu
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CompareButtonGpu(
                enabled = selectedGpus.size == 2,
                selectedCount = selectedGpus.size,
                onClick = { showComparison = true }
            )
        }
    }
}

@Composable
fun SearchBoxGpu(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        modifier = modifier,
        placeholder = {
            Text(
                "🔍 Buscar por nome da placa de vídeo...",
                color = Color.Gray.copy(alpha = 0.7f)
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = { onSearchQueryChanged("") }
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Limpar busca",
                        tint = Color.Gray
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1A1A1A),
            unfocusedContainerColor = Color(0xFF1A1A1A),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
            cursorColor = Color.White,
            focusedPlaceholderColor = Color.Gray.copy(alpha = 0.6f),
            unfocusedPlaceholderColor = Color.Gray.copy(alpha = 0.4f)
        ),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun SelectionBarGpu(
    selectedCount: Int,
    viewMode: ViewMode,
    onViewModeChanged: (ViewMode) -> Unit,
    onClearSelection: () -> Unit,
    searchQuery: String = "",
    filteredCount: Int = 0,
    totalCount: Int = 0,
    componentType: String = "GPU"
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Selecionadas: $selectedCount/2",
                        color = if (selectedCount == 2) Color.Green else Color.White,
                        fontWeight = FontWeight.Medium
                    )

                    if (selectedCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = onClearSelection,
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                        ) {
                            Text("Limpar", fontSize = 12.sp)
                        }
                    }
                }

                if (searchQuery.isNotEmpty()) {
                    Text(
                        "📊 Encontradas: $filteredCount de $totalCount ${componentType}s",
                        color = if (filteredCount > 0) Color.Cyan else Color.Yellow,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Row {
                FilterChip(
                    onClick = { onViewModeChanged(ViewMode.GRID) },
                    label = { Text("Grade") },
                    selected = viewMode == ViewMode.GRID,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.Blue,
                        selectedLabelColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                FilterChip(
                    onClick = { onViewModeChanged(ViewMode.LIST) },
                    label = { Text("Lista") },
                    selected = viewMode == ViewMode.LIST,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.Blue,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        if (searchQuery.isNotEmpty() && filteredCount == 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Yellow.copy(alpha = 0.1f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⚠️", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Nenhuma $componentType encontrada para \"$searchQuery\"",
                        color = Color.Yellow,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun GpuGridView(
    gpus: List<Gpu>,
    selectedGpus: List<Gpu>,
    onGpuSelected: (Gpu, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 280.dp),
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(gpus) { gpu ->
            GpuGridCard(
                gpu = gpu,
                isSelected = selectedGpus.contains(gpu),
                onSelected = { onGpuSelected(gpu, it) }
            )
        }
    }
}

@Composable
fun GpuListView(
    gpus: List<Gpu>,
    selectedGpus: List<Gpu>,
    onGpuSelected: (Gpu, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(gpus) { gpu ->
            GpuListCard(
                gpu = gpu,
                isSelected = selectedGpus.contains(gpu),
                onSelected = { onGpuSelected(gpu, it) }
            )
        }
    }
}

@Composable
fun GpuGridCard(
    gpu: Gpu,
    isSelected: Boolean,
    onSelected: (Boolean) -> Unit
) {
    val borderColor = when {
        isSelected -> Color.Green
        else -> Color.Gray.copy(alpha = 0.3f)
    }

    val backgroundColor = when {
        isSelected -> Color.Green.copy(alpha = 0.1f)
        else -> Color(0xFF1A1A1A)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onSelected(!isSelected) }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    gpu.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (isSelected) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Selecionada",
                        tint = Color.Green,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = if (gpu.brand == "AMD") Color.Red.copy(alpha = 0.2f) else Color.Green.copy(alpha = 0.2f),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(
                    gpu.brand,
                    color = if (gpu.brand == "AMD") Color.Red else Color.Green,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            GpuSpecRow("💾", "VRAM", "${gpu.memory} GB")
            GpuSpecRow("⚡", "Base", "${gpu.baseClock} MHz")
            GpuSpecRow("🚀", "Boost", "${gpu.turboClock} MHz")
            GpuSpecRow("💡", "TDP", "${gpu.tdp}W")
            GpuSpecRow("🌡️", "Temp", "${gpu.baseTemperature}°C")
        }
    }
}

@Composable
fun GpuListCard(
    gpu: Gpu,
    isSelected: Boolean,
    onSelected: (Boolean) -> Unit
) {
    val borderColor = if (isSelected) Color.Green else Color.Gray.copy(alpha = 0.3f)
    val backgroundColor = if (isSelected) Color.Green.copy(alpha = 0.1f) else Color(0xFF1A1A1A)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onSelected(!isSelected) },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    gpu.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${gpu.brand} • ${gpu.memory}GB • ${gpu.turboClock}MHz • ${gpu.tdp}W",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Selecionada",
                    tint = Color.Green,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun GpuSpecRow(emoji: String, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                emoji,
                fontSize = 14.sp,
                modifier = Modifier.width(20.dp)
            )
            Text(
                label,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        Text(
            value,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun CompareButtonGpu(
    enabled: Boolean,
    selectedCount: Int,
    onClick: () -> Unit,
    componentType: String = "GPU"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) Color.Green else Color.Gray,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            if (selectedCount == 2) "🔥 Comparar ${componentType}s!" else "Selecione 2 ${componentType}s ($selectedCount/2)",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ComparisonFullScreen1(
    gpu1: Gpu,
    gpu2: Gpu,
    benchmarkGpus: BenchmarkGpus,
    onBackClick: () -> Unit
) {
    val result = benchmarkGpus.compareGpus(gpu1, gpu2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            ),
            modifier = Modifier
                .height(48.dp)
                .align(Alignment.Start)
        ) {
            Text("← Voltar", fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    "Comparação Detalhada",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GpuInfoCard(gpu = gpu1)
                    Spacer(modifier = Modifier.height(24.dp))
                    GpuInfoCard(gpu = gpu2)
                }

                Spacer(modifier = Modifier.height(24.dp))

                ComparisonTableView1(
                    gpu1Name = gpu1.name,
                    gpu2Name = gpu2.name,
                    result = result
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ComparisonTableView1(
    gpu1Name: String,
    gpu2Name: String,
    result: Map<String, List<String>>,
    isLandscape: Boolean = false
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "📊 Comparativo Técnico",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Métrica",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(if (isLandscape) 1.5f else 2f),
                    fontSize = if (isLandscape) 12.sp else 14.sp
                )
                Text(
                    gpu1Name.take(if (isLandscape) 12 else 15) + if (gpu1Name.length > (if (isLandscape) 12 else 15)) "..." else "",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1.5f),
                    fontSize = if (isLandscape) 11.sp else 12.sp
                )
                Text(
                    gpu2Name.take(if (isLandscape) 12 else 15) + if (gpu2Name.length > (if (isLandscape) 12 else 15)) "..." else "",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1.5f),
                    fontSize = if (isLandscape) 11.sp else 12.sp
                )
                Text(
                    "Diferença",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    fontSize = if (isLandscape) 11.sp else 12.sp
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 2.dp,
                color = Color.Gray
            )

            result.forEach { (category, values) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2A2A2A)
                    )
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            category,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color.Cyan,
                            fontSize = if (isLandscape) 11.sp else 12.sp
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                values[0],
                                modifier = Modifier.weight(if (isLandscape) 1.5f else 2f),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                fontSize = if (isLandscape) 10.sp else 11.sp
                            )
                            Text(
                                values[1],
                                modifier = Modifier.weight(1.5f),
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                fontSize = if (isLandscape) 10.sp else 11.sp
                            )
                            Text(
                                values[2],
                                modifier = Modifier.weight(1.5f),
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                fontSize = if (isLandscape) 10.sp else 11.sp
                            )
                            Text(
                                values[3],
                                modifier = Modifier.weight(1f),
                                color = if (values[3].startsWith("+")) Color.Green else Color.Red,
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isLandscape) 10.sp else 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GpuInfoCard(gpu: Gpu, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                gpu.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("${gpu.memory} GB VRAM", color = Color.Gray)
            Text("Base: ${gpu.baseClock} MHz", color = Color.Gray)
            Text("Boost: ${gpu.turboClock} MHz", color = Color.Gray)
            Text("TDP: ${gpu.tdp}W", color = Color.Gray)
            Text("🌡️: ${gpu.baseTemperature}°C", color = Color.Gray)
        }
    }
}
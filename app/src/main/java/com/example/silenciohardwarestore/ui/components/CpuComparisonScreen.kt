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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silenciohardwarestore.data.Cpu
import com.example.silenciohardwarestore.data.CpuDataSource
import com.example.silenciohardwarestore.utils.BenchmarkUtils
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun CpuComparisonScreen(
    onBackClick: () -> Unit = {}
) {
    val cpus = remember { CpuDataSource.loadCpus().filter { it.name.isNotEmpty() } }
    var selectedCpus by remember { mutableStateOf<List<Cpu>>(emptyList()) }
    val benchmarkUtils = remember { BenchmarkUtils() }
    var showComparison by remember { mutableStateOf(false) }
    var viewMode by remember { mutableStateOf(ViewMode.GRID) }
    var searchQuery by remember { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    val density = LocalDensity.current

    val filteredCpus = remember(cpus, searchQuery) {
        if (searchQuery.isBlank()) {
            cpus
        } else {
            cpus.filter { cpu ->
                cpu.name.contains(searchQuery, ignoreCase = true) ||
                        cpu.brand.contains(searchQuery, ignoreCase = true) ||
                        cpu.socket.contains(searchQuery, ignoreCase = true) ||
                        cpu.cores.toString().contains(searchQuery) ||
                        cpu.tdp.toString().contains(searchQuery)
            }
        }
    }

    if (showComparison && selectedCpus.size == 2) {
        ComparisonFullScreen(
            cpu1 = selectedCpus[0],
            cpu2 = selectedCpus[1],
            benchmarkUtils = benchmarkUtils,
            onBackClick = {
                showComparison = false
                selectedCpus = emptyList()
                searchQuery = ""
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Header responsivo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(if (isLandscape) 8.dp else 16.dp)
                    .padding(top = if (isLandscape) 8.dp else 16.dp)
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
                        modifier = Modifier.height(if (isLandscape) 40.dp else 48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White,
                            modifier = Modifier.size(if (isLandscape) 14.dp else 16.dp)
                        )
                        if (!isLandscape) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Voltar", fontSize = if (isLandscape) 12.sp else 14.sp)
                        }
                    }

                    Text(
                        "Comparar CPUs",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isLandscape) 18.sp else MaterialTheme
                            .typography.headlineMedium.fontSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(if (isLandscape) 8.dp else 48.dp))
                }

                Spacer(modifier = Modifier.height(if (isLandscape) 8.dp else 16.dp))

                if (!isLandscape) {
                    Text(
                        "Compare CPUs lado a lado",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = if (isLandscape) 14.sp else MaterialTheme
                            .typography.titleMedium.fontSize
                    )

                    Spacer(modifier = Modifier.height(if (isLandscape) 8.dp else 24.dp))
                }

                SearchBox(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = { newQuery ->
                        searchQuery = newQuery
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(if (isLandscape) 8.dp else 16.dp))

                SelectionBar(
                    selectedCount = selectedCpus.size,
                    viewMode = viewMode,
                    onViewModeChanged = { viewMode = it },
                    onClearSelection = {
                        selectedCpus = emptyList()
                        searchQuery = ""
                    },
                    searchQuery = searchQuery,
                    filteredCount = filteredCpus.size,
                    totalCount = cpus.size,
                    isLandscape = isLandscape
                )
            }

            // Conteúdo scrollável
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (viewMode) {
                    ViewMode.GRID -> CpuGridView(
                        cpus = filteredCpus,
                        selectedCpus = selectedCpus,
                        onCpuSelected = { cpu, isSelected ->
                            selectedCpus = if (isSelected) {
                                if (selectedCpus.size < 2) {
                                    selectedCpus + cpu
                                } else {
                                    selectedCpus
                                }
                            } else {
                                selectedCpus - cpu
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                        isLandscape = isLandscape
                    )

                    ViewMode.LIST -> CpuListView(
                        cpus = filteredCpus,
                        selectedCpus = selectedCpus,
                        onCpuSelected = { cpu, isSelected ->
                            selectedCpus = if (isSelected) {
                                if (selectedCpus.size < 2) selectedCpus + cpu else selectedCpus
                            } else {
                                selectedCpus - cpu
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Bottom bar responsivo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(
                        start = if (isLandscape) 8.dp else 16.dp,
                        end = if (isLandscape) 8.dp else 16.dp,
                        top = if (isLandscape) 8.dp else 16.dp,
                        bottom = if (isLandscape) 16.dp else 32.dp
                    )
            ) {
                CompareButton(
                    enabled = selectedCpus.size == 2,
                    selectedCount = selectedCpus.size,
                    onClick = { showComparison = true },
                    isLandscape = isLandscape
                )
            }
        }
    }
}

@Composable
fun SearchBox(
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
                "🔍 Buscar por nome do processador...",
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
fun SelectionBar(
    selectedCount: Int,
    viewMode: ViewMode,
    onViewModeChanged: (ViewMode) -> Unit,
    onClearSelection: () -> Unit,
    searchQuery: String = "",
    filteredCount: Int = 0,
    totalCount: Int = 0,
    isLandscape: Boolean = false
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
                        fontWeight = FontWeight.Medium,
                        fontSize = if (isLandscape) 12.sp else 14.sp
                    )

                    if (selectedCount > 0) {
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(
                            onClick = onClearSelection,
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                        ) {
                            Text("Limpar", fontSize = if (isLandscape) 10.sp else 12.sp)
                        }
                    }
                }

                if (searchQuery.isNotEmpty()) {
                    Text(
                        "📊 Encontradas: $filteredCount de $totalCount CPUs",
                        color = if (filteredCount > 0) Color.Cyan else Color.Yellow,
                        fontSize = if (isLandscape) 10.sp else 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Row {
                FilterChip(
                    onClick = { onViewModeChanged(ViewMode.GRID) },
                    label = {
                        Text("Grade", fontSize = if (isLandscape) 10.sp else 12.sp)
                    },
                    selected = viewMode == ViewMode.GRID,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.Blue,
                        selectedLabelColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.width(4.dp))

                FilterChip(
                    onClick = { onViewModeChanged(ViewMode.LIST) },
                    label = {
                        Text("Lista", fontSize = if (isLandscape) 10.sp else 12.sp)
                    },
                    selected = viewMode == ViewMode.LIST,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.Blue,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        if (searchQuery.isNotEmpty() && filteredCount == 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Yellow.copy(alpha = 0.1f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⚠️", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Nenhuma CPU encontrada para \"$searchQuery\"",
                        color = Color.Yellow,
                        fontSize = if (isLandscape) 10.sp else 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CpuGridView(
    cpus: List<Cpu>,
    selectedCpus: List<Cpu>,
    onCpuSelected: (Cpu, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isLandscape: Boolean = false
) {

    val columns = if (isLandscape) 3 else 2

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier,
        contentPadding = PaddingValues(if (isLandscape) 4.dp else 8.dp),
        horizontalArrangement = Arrangement.spacedBy(if (isLandscape) 4.dp else 8.dp),
        verticalArrangement = Arrangement.spacedBy(if (isLandscape) 4.dp else 8.dp)
    ) {
        items(cpus) { cpu ->
            CpuGridCard(
                cpu = cpu,
                isSelected = selectedCpus.contains(cpu),
                onSelected = { onCpuSelected(cpu, it) },
                isLandscape = isLandscape
            )
        }
    }
}

@Composable
fun CpuListView(
    cpus: List<Cpu>,
    selectedCpus: List<Cpu>,
    onCpuSelected: (Cpu, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cpus) { cpu ->
            CpuListCard(
                cpu = cpu,
                isSelected = selectedCpus.contains(cpu),
                onSelected = { onCpuSelected(cpu, it) }
            )
        }
    }
}

@Composable
fun CpuGridCard(
    cpu: Cpu,
    isSelected: Boolean,
    onSelected: (Boolean) -> Unit,
    isLandscape: Boolean = false
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
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onSelected(!isSelected) }
            .padding(if (isLandscape) 8.dp else 12.dp)
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
                    cpu.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = if (isLandscape) 1 else 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = if (isLandscape) 12.sp else 14.sp,
                    modifier = Modifier.weight(1f)
                )

                if (isSelected) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Selecionada",
                        tint = Color.Green,
                        modifier = Modifier.size(if (isLandscape) 16.dp else 20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(if (isLandscape) 4.dp else 6.dp))

            Surface(
                color = if (cpu.brand == "AMD") Color.Red.copy(alpha = 0.2f) else Color.Blue.copy(alpha = 0.2f),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(
                    cpu.brand,
                    color = if (cpu.brand == "AMD") Color.Red else Color.Blue,
                    fontSize = if (isLandscape) 10.sp else 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }

            Spacer(modifier = Modifier.height(if (isLandscape) 6.dp else 8.dp))

            CpuSpecRow("⚡", "Base", "${cpu.baseClock} GHz", isLandscape)
            CpuSpecRow("🚀", "Turbo", "${cpu.turboClock} GHz", isLandscape)
            CpuSpecRow("🔧", "Núcleos", "${cpu.cores}C/${cpu.threads}T", isLandscape)
            CpuSpecRow("💡", "TDP", "${cpu.tdp}W", isLandscape)
            CpuSpecRow("🌡️", "Temp", "${cpu.baseTemperature}°C", isLandscape)
        }
    }
}

@Composable
fun CpuListCard(
    cpu: Cpu,
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
                    cpu.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "${cpu.brand} • ${cpu.cores}C/${cpu.threads}T • ${cpu.turboClock}GHz • ${cpu.tdp}W",
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
fun CpuSpecRow(emoji: String, label: String, value: String, isLandscape: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                emoji,
                fontSize = if (isLandscape) 10.sp else 12.sp,
                modifier = Modifier.width(if (isLandscape) 16.dp else 20.dp)
            )
            Text(
                label,
                color = Color.Gray,
                fontSize = if (isLandscape) 10.sp else 12.sp
            )
        }

        Text(
            value,
            color = Color.White,
            fontSize = if (isLandscape) 10.sp else 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
    Spacer(modifier = Modifier.height(if (isLandscape) 2.dp else 4.dp))
}

@Composable
fun CompareButton(
    enabled: Boolean,
    selectedCount: Int,
    onClick: () -> Unit,
    isLandscape: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isLandscape) 44.dp else 56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) Color.Green else Color.Gray,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(if (isLandscape) 16.dp else 20.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            if (selectedCount == 2) "🔥 Comparar CPUs!" else "Selecione 2 CPUs ($selectedCount/2)",
            fontWeight = FontWeight.Bold,
            fontSize = if (isLandscape) 12.sp else 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ComparisonFullScreen(
    cpu1: Cpu,
    cpu2: Cpu,
    benchmarkUtils: BenchmarkUtils,
    onBackClick: () -> Unit
) {
    val result = benchmarkUtils.compareCpus(cpu1, cpu2)

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
                    CpuInfoCard(cpu = cpu1)
                    Spacer(modifier = Modifier.height(24.dp))
                    CpuInfoCard(cpu = cpu2)
                }

                Spacer(modifier = Modifier.height(24.dp))

                ComparisonTableView(
                    cpu1Name = cpu1.name,
                    cpu2Name = cpu2.name,
                    result = result
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ComparisonTableView(
    cpu1Name: String,
    cpu2Name: String,
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
                    cpu1Name.take(if (isLandscape) 12 else 15) + if (cpu1Name.length > (if (isLandscape) 12 else 15)) "..." else "",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1.5f),
                    fontSize = if (isLandscape) 11.sp else 12.sp
                )
                Text(
                    cpu2Name.take(if (isLandscape) 12 else 15) + if (cpu2Name.length > (if (isLandscape) 12 else 15)) "..." else "",
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
fun CpuInfoCard(cpu: Cpu, modifier: Modifier = Modifier) {
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
                cpu.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("${cpu.cores} cores / ${cpu.threads} threads", color = Color.Gray)
            Text("Base: ${cpu.baseClock} GHz", color = Color.Gray)
            Text("Turbo: ${cpu.turboClock} GHz", color = Color.Gray)
            Text("TDP: ${cpu.tdp}W", color = Color.Gray)
        }
    }
}

enum class ViewMode {
    GRID, LIST
}
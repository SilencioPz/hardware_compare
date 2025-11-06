package com.silenciopz.hardware.utils

import com.silenciopz.hardware.data.Cpu
import com.silenciopz.hardware.data.CpuDataSource
import com.silenciopz.hardware.data.Gpu
import com.silenciopz.hardware.data.Game
import com.silenciopz.hardware.data.GpuDataSource
import kotlin.math.abs

class BenchmarkGames {
    fun calculateGamePerformance(
        cpu: Cpu,
        gpu: Gpu,
        game: Game,
        resolution: String = "1920x1080"
    ): Map<String, List<String>> {
        val bottleneckResult = calculateBottleneck(cpu, gpu, game, resolution)
        val estimatedFps = estimateFps(cpu, gpu, game, resolution)
        val thermalRisk = calculateThermalRisk(cpu, gpu, game)

        return mapOf(
            "🎮 Desempenho no Jogo" to listOf(
                game.name,
                "${estimatedFps.first}FPS",
                "${estimatedFps.second}FPS",
                compareFpsValues(estimatedFps.first, estimatedFps.second)
            ),
            "⚡ Bottleneck" to listOf(
                "Gargalo do Sistema",
                "${"%.1f".format(bottleneckResult.first)}%",
                "${bottleneckResult.second}",
                getBottleneckSeverity(bottleneckResult.first)
            ),
            "🔧 Configuração Recomendada" to listOf(
                "Pelo jogo",
                game.recommendedCpu,
                game.recommendedGpu,
                compareWithRecommended(cpu.name, gpu.name, game)
            ),
            "🌡️ Temperatura Estimada" to listOf(
                "Sob carga",
                "${estimateGamingTemp(cpu, game)}°C / ${estimateGamingTemp(gpu, game)}°C",
                "CPU / GPU",
                getThermalWarning(cpu, gpu, game)
            ),
            "⚠️ Risco Térmico" to listOf(
                "Chance de throttle",
                "${thermalRisk.first}% / ${thermalRisk.second}%",
                "CPU / GPU",
                getOverallThermalRisk(thermalRisk)
            )
        )
    }

    fun calculateBottleneck(
        cpu: Cpu,
        gpu: Gpu,
        game: Game,
        resolution: String
    ): Pair<Float, String> {

        val isLightGame = game.bottleneckMultiplier < 0.8f
        val isLowResolution = resolution == "1024x768"
        val isModernHardware = calculateCpuGamingScore(cpu) > 3000 && calculateGpuGamingScore(gpu) > 3000

        if (isLightGame && isLowResolution && isModernHardware) {
            return Pair(0f, "Hardware Overkill")
        }

        val isAncientGame = game.bottleneckMultiplier < 0.4f

        if (isAncientGame && isModernHardware) {
            return Pair(0f, "Hardware Overkill")
        }

        if (isOverkillForGame(cpu, gpu, game)) {
            return Pair(0f, "Hardware Overkill")
        }

        val cpuScore = calculateCpuGamingScore(cpu).toFloat()
        val gpuScore = calculateGpuGamingScore(gpu).toFloat()

        val isHighEndCpu = cpu.name.contains("Ryzen 9", ignoreCase = true) ||
                cpu.name.contains("Ryzen 7 58", ignoreCase = true) ||
                (cpu.name.contains("Ryzen 7", ignoreCase = true) &&
                        !cpu.name.contains("G", ignoreCase = true) &&
                        (cpu.name.contains("5700X", ignoreCase = true) ||
                                cpu.name.contains("5800", ignoreCase = true))) ||
                cpu.name.contains("X3D", ignoreCase = true) && !cpu.name
                    .contains("Ryzen 5", ignoreCase = true) ||
                cpu.name.contains("i9", ignoreCase = true) ||
                cpu.name.contains("i7-12700", ignoreCase = true) ||
                cpu.name.contains("i7-13700", ignoreCase = true) ||
                cpu.name.contains("i7-14700", ignoreCase = true) ||
                (cpu.cores >= 12 && cpu.baseClock >= 3.8f)

        val cpuIntensityFactor = game.cpuIntensity * 0.5f
        val gpuIntensityFactor = game.gpuIntensity * 0.5f

        val adjustedCpuScore = if (isLightGame) {
            cpuScore * 0.3f
        } else {
            cpuScore * (0.5f + cpuIntensityFactor)
        }

        val adjustedGpuScore = if (isLightGame) {
            gpuScore * 0.2f
        } else {
            gpuScore * (0.5f + gpuIntensityFactor)
        }

        val resolutionMultiplier = getResolutionMultiplier(resolution)
        val finalGpuScore = adjustedGpuScore * resolutionMultiplier

        val resolutionBias = 1.0f

        val bottleneckPercent = if (finalGpuScore > adjustedCpuScore) {
            ((finalGpuScore - adjustedCpuScore) / finalGpuScore * 100f * resolutionBias).coerceAtMost(99f)
        } else {
            -((adjustedCpuScore - finalGpuScore) / adjustedCpuScore * 100f * resolutionBias).coerceAtMost(99f)
        }

        val severity = when {
            abs(bottleneckPercent) < 5f -> "Equilibrado"
            bottleneckPercent > 0f -> when {
                bottleneckPercent < 15f -> "Leve (CPU limitando)"
                bottleneckPercent < 30f -> "Moderado (CPU limitando)"
                bottleneckPercent < 50f -> "Significativo (CPU limitando)"
                else -> "Extremo (CPU limitando)"
            }

            else -> when {
                abs(bottleneckPercent) < 15f -> "Leve (GPU limitando)"
                abs(bottleneckPercent) < 30f -> "Moderado (GPU limitando)"
                abs(bottleneckPercent) < 50f -> "Significativo (GPU limitando)"
                else -> "Extremo (GPU limitando)"
            }
        }

        println("DEBUG: CPU: ${cpu.name}, Score: $cpuScore")
        println("DEBUG: GPU: ${gpu.name}, Score: $gpuScore")
        println("DEBUG: Game: ${game.name}, CPU Intensity: ${game.cpuIntensity}, GPU Intensity: ${game.gpuIntensity}")
        println("DEBUG: Resolution: $resolution, Multiplier: $resolutionMultiplier, Bias: $resolutionBias")
        println("DEBUG: Adjusted CPU: $adjustedCpuScore, Adjusted GPU: $finalGpuScore")
        println("DEBUG: Final Bottleneck: $bottleneckPercent%, Severity: $severity")
        println("DEBUG: isHighEndCpu: $isHighEndCpu")
        println("DEBUG: ------------------------------------")

        return Pair(bottleneckPercent, severity)
    }

    fun calculateCpuGamingScore(cpu: Cpu): Int {
        val baseClock = if (cpu.turboClock > 0f) cpu.turboClock else cpu.baseClock

        var baseScore = (baseClock * 800f).toInt()

        val coreBonus = when (cpu.cores) {
            in 1..2 -> 0.6f
            in 3..4 -> 0.8f
            in 5..6 -> 1.0f
            in 7..8 -> 1.2f
            in 9..12 -> 1.4f
            in 13..16 -> 1.6f
            else -> 1.8f
        }

        val seriesBonus = when {
            cpu.name.contains("Ryzen 9", ignoreCase = true) -> 1.8f
            cpu.name.contains("Ryzen 7", ignoreCase = true) -> 1.4f
            cpu.name.contains("Ryzen 5", ignoreCase = true) -> 1.1f
            cpu.name.contains("Ryzen 3", ignoreCase = true) -> 0.9f
            cpu.name.contains("G") && cpu.name.contains
                ("Ryzen", ignoreCase = true) -> 0.85f
            else -> 1.0f
        }

        val generationBonus = when {
            cpu.name.contains("9950", ignoreCase = true) -> 1.5f
            cpu.name.contains("9900", ignoreCase = true) -> 1.4f
            cpu.name.contains("9800", ignoreCase = true) -> 1.3f
            cpu.name.contains("5800", ignoreCase = true) -> 1.2f
            cpu.name.contains("5700", ignoreCase = true) -> 1.15f
            cpu.name.contains("5600", ignoreCase = true) -> 1.1f
            cpu.name.contains("3600", ignoreCase = true) -> 1.0f
            else -> 1.0f
        }

        val cacheBonus = when {
            cpu.name.contains("X3D", ignoreCase = true) -> 1.8f
            cpu.name.contains("G", ignoreCase = true) -> 0.9f
            else -> 1.0f
        }

        return (baseScore * coreBonus * seriesBonus * generationBonus * cacheBonus).toInt()
    }

    fun calculateGpuGamingScore(gpu: Gpu): Int {
        val clockToUse = gpu.effectiveClock

        val clockPart = clockToUse * 0.002f
        val memoryPart = gpu.memory * 0.8f

        val baseScore = (clockPart + memoryPart).toInt()

        println("DEBUG: clockToUse=$clockToUse, clockPart=$clockPart, memoryPart=$memoryPart, " +
                "baseScore=$baseScore")

        val architectureBonus = when {
            gpu.name.contains("RTX 50", ignoreCase = true) -> 1.8f
            gpu.name.contains("RTX 40", ignoreCase = true) -> 1.5f
            gpu.name.contains("RTX 30", ignoreCase = true) -> 1.2f
            gpu.name.contains("RTX 20", ignoreCase = true) -> 1.0f
            gpu.name.contains("RX 7", ignoreCase = true) -> 1.4f
            gpu.name.contains("RX 6", ignoreCase = true) -> 1.1f
            gpu.name.contains("RX 5", ignoreCase = true) -> 0.9f
            gpu.name.contains("GTX 16", ignoreCase = true) -> 0.7f
            gpu.name.contains("GTX", ignoreCase = true) -> 0.6f
            else -> 1.0f
        }

        val vramBonus = when (gpu.memory) {
            in 4..6 -> 0.8f
            in 7..8 -> 1.0f
            in 9..12 -> 1.2f
            in 13..16 -> 1.4f
            in 17..24 -> 1.6f
            else -> 1.0f
        }

        val busWidthBonus = when {
            gpu.name.contains("6600", ignoreCase = true) -> 0.9f
            gpu.name.contains("6700", ignoreCase = true) -> 1.0f
            gpu.name.contains("6800", ignoreCase = true) -> 1.2f
            gpu.name.contains("6900", ignoreCase = true) -> 1.3f
            gpu.name.contains("3070", ignoreCase = true) -> 1.1f
            gpu.name.contains("3080", ignoreCase = true) -> 1.3f
            gpu.name.contains("3090", ignoreCase = true) -> 1.4f
            else -> 1.0f
        }

        val finalScore = (baseScore * architectureBonus * vramBonus * busWidthBonus).toInt()
        println("DEBUG: GPU Final Score: $finalScore")
        return finalScore
    }

    private fun estimateFps(cpu: Cpu, gpu: Gpu, game: Game, resolution: String): Pair<Int, Int> {

        return Pair(200, 200)

    }

    private fun getResolutionMultiplier(resolution: String): Float {
        return when (resolution) {
            "1024x768" -> 0.3f   // Favorece CPU
            "1920x1080" -> 1.0f
            "2560x1440" -> 2.0f
            "3840x2160" -> 3.0f  // Favorece GPU
            else -> 1.0f
        }
    }

    private fun estimateGamingTemp(component: Any, game: Game? = null): Int {
        return when (component) {
            is Cpu -> {
                val base = component.baseTemperature
                val gameIntensityFactor = game?.cpuIntensity ?: 1.0f

                val loadIncrease = when {
                    gameIntensityFactor > 0.8f -> (component.tdp * 0.4f).toInt() // Jogos pesados
                    gameIntensityFactor > 0.5f -> (component.tdp * 0.3f).toInt() // Jogos médios
                    else -> (component.tdp * 0.2f).toInt() // Jogos leves
                }

                val estimatedTemp = base + loadIncrease

                val maxReasonableTemp = when {
                    component.tdp > 150 -> 85 // CPUs high-end
                    component.tdp > 100 -> 80 // CPUs mid-range
                    else -> 75 // CPUs eficientes
                }

                println("DEBUG TEMP: CPU ${component.name} - Base: ${base}°C, TDP: ${component.tdp}W, " +
                        "Game Intensity: ${gameIntensityFactor}, Load: ${loadIncrease}°C, Final: ${estimatedTemp}°C)"
                )

                estimatedTemp.coerceAtMost(maxReasonableTemp).coerceAtLeast(base)

            }

            is Gpu -> {
                val base = component.baseTemperature
                val gameIntensityFactor = game?.gpuIntensity ?: 1.0f

                val loadIncrease = when {
                    gameIntensityFactor > 0.8f -> (component.tdp * 0.3f).toInt() // Jogos pesados
                    gameIntensityFactor > 0.5f -> (component.tdp * 0.2f).toInt() // Jogos médios
                    else -> (component.tdp * 0.1f).toInt() // Jogos leves
                }

                val estimatedTemp = base + loadIncrease

                val maxReasonableTemp = when {
                    component.tdp > 200 -> 85 // GPUs high-end com cooling robusto
                    component.tdp > 120 -> 80 // GPUs mid-range
                    else -> 75 // GPUs entry-level
                }

                println("DEBUG TEMP: GPU ${component.name} - Base: ${base}°C, TDP: ${component.tdp}W, " +
                        "Game Intensity: ${gameIntensityFactor}, Load: ${loadIncrease}°C, Final: ${estimatedTemp}°C")

                estimatedTemp.coerceAtMost(maxReasonableTemp).coerceAtLeast(base)
            }

            else -> 65
        }
    }

    private fun calculateThermalRisk(cpu: Cpu, gpu: Gpu, game: Game): Pair<Int, Int> {
        val cpuTemp = estimateGamingTemp(cpu, game)
        val gpuTemp = estimateGamingTemp(gpu, game)

        val cpuRisk = when {
            cpuTemp >= cpu.thermalThrottleTemp -> 90
            cpuTemp >= cpu.maxSafeTemperature -> 75
            cpuTemp >= 85 -> 60
            cpuTemp >= 80 -> 45
            cpuTemp >= 75 -> 30
            cpuTemp >= 70 -> 20
            cpuTemp >= 65 -> 10
            else -> 5
        }

        val gpuRisk = when {
            gpuTemp >= gpu.thermalThrottleTemp -> 90
            gpuTemp >= gpu.maxSafeTemperature -> 80
            gpuTemp >= 85 -> 65
            gpuTemp >= 80 -> 50
            gpuTemp >= 75 -> 35
            gpuTemp >= 70 -> 20
            gpuTemp >= 65 -> 10
            else -> 5
        }

        println("DEBUG RISCO: CPU ${cpu.name} - Temp: ${cpuTemp}°C, Risco: ${cpuRisk}%")
        println("DEBUG RISCO: GPU ${gpu.name} - Temp: ${gpuTemp}°C, Risco: ${gpuRisk}%")

        return Pair(cpuRisk, gpuRisk)
    }

    private fun getThermalWarning(cpu: Cpu, gpu: Gpu, game: Game): String {
        val cpuTemp = estimateGamingTemp(cpu, game)
        val gpuTemp = estimateGamingTemp(gpu, game)

        val cpuStatus = when {
            cpuTemp >= cpu.thermalThrottleTemp -> "CPU: Crítico ⚠️"
            cpuTemp >= cpu.maxSafeTemperature -> "CPU: Alto 🔥"
            cpuTemp >= 80 -> "CPU: Elevado 🔶"
            cpuTemp >= 70 -> "CPU: Quente 🟡"
            else -> "CPU: Normal ✅"
        }

        val gpuStatus = when {
            gpuTemp >= gpu.thermalThrottleTemp -> "GPU: Crítico ⚠️"
            gpuTemp >= gpu.maxSafeTemperature -> "GPU: Alto 🔥"
            gpuTemp >= 80 -> "GPU: Elevado 🔶"
            gpuTemp >= 70 -> "GPU: Quente 🟡"
            else -> "GPU: Normal ✅"
        }

        println("DEBUG WARNING: CPU ${cpuTemp}°C/GPU ${gpuTemp}°C - Avaliação: $cpuStatus / $gpuStatus")

        return "$cpuStatus / $gpuStatus"
    }



    private fun getOverallThermalRisk(thermalRisk: Pair<Int, Int>): String {
        val cpuRisk = thermalRisk.first
        val gpuRisk = thermalRisk.second

        return when {
            cpuRisk >= 80 || gpuRisk >= 80 -> "🔥 Alto Risco (Throttling provável)"
            cpuRisk >= 60 || gpuRisk >= 60 -> "⚠️ Médio Risco (Monitorar temperaturas)"
            cpuRisk >= 40 || gpuRisk >= 40 -> "🔶 Risco Moderado (Atenção necessária)"
            cpuRisk >= 20 || gpuRisk >= 20 -> "🟡 Aquecimento Normal (Sistema estável)"
            else -> "✅ Baixo Risco (Temperaturas seguras)"
        }
    }

    private fun compareFpsValues(fps1: Int, fps2: Int): String {
        val avgFps = (fps1 + fps2) / 2
        return when {
            avgFps >= 200 -> "🎯 Competitivo (200 FPS)"
            avgFps > 144 -> "🚀 Muito Fluido"
            avgFps > 90 -> "✅ Fluido"
            avgFps > 60 -> "⚠️ Limitado"
            else -> "❌ Inviável"
        }
    }

    private fun getBottleneckSeverity(bottleneck: Float): String {
        return when {
            abs(bottleneck) < 8 -> "✅ Balanceado"
            bottleneck > 0 -> when {
                bottleneck < 15 -> "⚠️ Leve (CPU)"
                bottleneck < 30 -> "🔶 Moderado (CPU)"
                bottleneck < 50 -> "🔴 Significativo (CPU)"
                else -> "💀 Extremo (CPU)"
            }
            else -> when {
                abs(bottleneck) < 15 -> "⚠️ Leve (GPU)"
                abs(bottleneck) < 30 -> "🔶 Moderado (GPU)"
                abs(bottleneck) < 50 -> "🔴 Significativo (GPU)"
                else -> "💀 Extremo (GPU)"
            }
        }
    }

    private fun compareWithRecommended(cpu: String, gpu: String, game: Game): String {
        val cpuObj = CpuDataSource.loadCpus().find { it.name == cpu }
        val gpuObj = GpuDataSource.loadGpus().find { it.name == gpu }

        if (cpuObj != null && gpuObj != null) {
            val isOverkill = isOverkillForGame(cpuObj, gpuObj, game)
            if (isOverkill) {
                return "🚀 Hardware excessivo"
            }
        }

        val cpuMatch = cpu.contains(game.recommendedCpu.split(" ").last(), ignoreCase = true)
        val gpuMatch = gpu.contains(game.recommendedGpu.split(" ").last(), ignoreCase = true)

        return when {
            cpuMatch && gpuMatch -> "✅ Atende ou supera"
            cpuMatch || gpuMatch -> "⚠️ Parcialmente atende"
            else -> "❌ Abaixo do recomendado"
        }
    }



    private fun getCpuRecommendation(bottleneck: Float, cpu: Cpu): String {

        val isHighEndCpu = cpu.name.contains("Ryzen 9", ignoreCase = true) ||
                cpu.name.contains("Ryzen 7 58", ignoreCase = true) ||
                (cpu.name.contains("Ryzen 7 57", ignoreCase = true) &&
                        !cpu.name.contains("G", ignoreCase = true)) ||
                cpu.name.contains("X3D", ignoreCase = true) ||
                cpu.name.contains("i9", ignoreCase = true) ||
                (cpu.name.contains("i7", ignoreCase = true) &&
                        (cpu.name.contains("12700", ignoreCase = true) ||
                                cpu.name.contains("13700", ignoreCase = true) ||
                                cpu.name.contains("14700", ignoreCase = true))) ||
                (cpu.cores >= 12 && cpu.baseClock >= 3.8f)

        val isMidHighCpu = cpu.name.contains("Ryzen 7", ignoreCase = true) ||
                cpu.name.contains("Ryzen 5 5600X", ignoreCase = true) ||
                cpu.name.contains("Ryzen 5 5700X", ignoreCase = true) ||
                (cpu.name.contains("i5", ignoreCase = true) && cpu.cores >= 6)

        val is5700G = cpu.name.contains("5700G", ignoreCase = true)

        return when {
            bottleneck > 30 -> "💀 CPU insuficiente para jogos pesados"
            bottleneck > 20 -> "⚠️ CPU pode limitar significativamente"
            bottleneck > 15 -> {
                when {
                    isHighEndCpu -> "💡 CPU boa, mas pode limitar em alguns cenários"
                    isMidHighCpu && !is5700G -> "⚠️ CPU pode limitar em jogos pesados"
                    is5700G -> "⚠️ 5700G adequado para maioria dos jogos, mas pode limitar em titles AAA"
                    else -> "⚠️ CPU pode limitar em jogos pesados"
                }
            }
            bottleneck > 8 -> {
                when {
                    isHighEndCpu -> "✅ CPU excelente para maioria dos jogos"
                    isMidHighCpu -> "💡 CPU adequada para gaming"
                    else -> "💡 CPU adequada para gaming casual"
                }
            }
            bottleneck > 0 -> {
                when {
                    isHighEndCpu -> "🚀 CPU overkill - excelente"
                    isMidHighCpu && !is5700G -> "✅ CPU boa para gaming"
                    is5700G -> "✅ 5700G muito bom para gaming 1080p"
                    else -> "✅ CPU adequada para gaming"
                }
            }
            bottleneck < -30 -> "✅ CPU mais que suficiente - GPU é o limitante"
            bottleneck < -15 -> "✅ CPU adequado - GPU limitando moderadamente"
            bottleneck < -8 -> "✅ CPU balanceado - GPU limitando levemente"
            else -> "✅ Configuração equilibrada"
        }
    }


    private fun getGpuRecommendation(bottleneck: Float, gpu: Gpu, game: Game): String {
        val isDemandingGame = game.bottleneckMultiplier > 1.0f
        val absoluteBottleneck = abs(bottleneck)

        return when {
            absoluteBottleneck > 40 -> "💀 GPU insuficiente para este jogo"
            absoluteBottleneck > 25 -> "⚠️ GPU pode limitar significativamente"
            absoluteBottleneck > 15 -> "💡 GPU adequada para maioria dos jogos"
            isDemandingGame && gpu.memory < 12 -> "⚠️ GPU boa, mas VRAM pode limitar em jogos pesados"
            absoluteBottleneck > 8 -> "✅ GPU boa para gaming"
            else -> "✅ GPU excelente para gaming"
        }
    }

    private fun getOverallRecommendation(bottleneck: Float): String {
        val absoluteBottleneck = abs(bottleneck)
        return when {
            absoluteBottleneck < 5 -> "✅ Configuração perfeitamente balanceada"
            absoluteBottleneck < 10 -> "⚡ Configuração bem equilibrada"
            absoluteBottleneck < 20 -> "💡 ${if (bottleneck > 0) "CPU" else "GPU"} pode limitar levemente"
            absoluteBottleneck < 30 -> "⚠️ ${if (bottleneck > 0) "CPU" else "GPU"} limitando moderadamente"
            else -> "🔴 ${if (bottleneck > 0) "CPU" else "GPU"} limitando significativamente"
        }
    }

    private fun getIdealResolution(cpu: Cpu, gpu: Gpu): String {
        val cpuPower = calculateCpuGamingScore(cpu)
        val gpuPower = calculateGpuGamingScore(gpu)
        val ratio = gpuPower.toFloat() / cpuPower.toFloat()

        return when {
            ratio > 1.8 -> "3840x2160"  // 4K
            ratio > 1.2 -> "2560x1440"  // 1440p
            ratio > 0.7 -> "1920x1080"  // 1080p
            else -> "1024x768"          // 768p
        }
    }

    private fun isOverkillForGame(cpu: Cpu, gpu: Gpu, game: Game): Boolean {
        val cpuScore = calculateCpuGamingScore(cpu)
        val gpuScore = calculateGpuGamingScore(gpu)

        if (game.bottleneckMultiplier < 0.5f && cpuScore > 4000 && gpuScore > 4000) {
            return true
        }

        if (game.bottleneckMultiplier < 0.8f && cpuScore > 5000 && gpuScore > 5000) {
            return true
        }

        return false
    }
}
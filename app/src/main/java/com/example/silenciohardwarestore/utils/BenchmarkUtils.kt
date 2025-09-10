package com.example.silenciohardwarestore.utils

import com.example.silenciohardwarestore.data.Cpu

class BenchmarkUtils {

    // Retorna um mapa com as comparações lado a lado + diferença em %
    fun compareCpus(cpu1: Cpu, cpu2: Cpu): Map<String, List<String>> {
        return mapOf(
            "Performance" to listOf(
                "Overall Score",
                "${calculateScore(cpu1)}",
                "${calculateScore(cpu2)}",
                "${getDifference(calculateScore(cpu1), calculateScore(cpu2))}%"
            ),
            "Single-Core" to listOf(
                "Single Thread Score",
                "${calculateSingleCoreScore(cpu1)}",
                "${calculateSingleCoreScore(cpu2)}",
                "${getDifference(calculateSingleCoreScore(cpu1), calculateSingleCoreScore(cpu2))}%"
            ),
            "Multi-Core" to listOf(
                "Multi Thread Score",
                "${calculateMultiCoreScore(cpu1)}",
                "${calculateMultiCoreScore(cpu2)}",
                "${getDifference(calculateMultiCoreScore(cpu1), calculateMultiCoreScore(cpu2))}%"
            ),
            "Gaming" to listOf(
                "FPS Estimado (1080p)",
                "${estimateFps(cpu1)}",
                "${estimateFps(cpu2)}",
                "${getDifference(estimateFps(cpu1), estimateFps(cpu2))}%"
            ),
            "Eficiência" to listOf(
                "TDP (W)",
                "${cpu1.tdp}",
                "${cpu2.tdp}",
                if (cpu1.tdp > cpu2.tdp) "+${cpu1.tdp - cpu2.tdp}W" else "-${cpu2.tdp - cpu1.tdp}W"
            ),
            "🌡️ Temp. Gaming/Trabalho Pesado" to listOf(
                "Temperatura estimada",
                "${estimateHeavyWorkloadTemp(cpu1)}°C",
                "${estimateHeavyWorkloadTemp(cpu2)}°C",
                compareThermalRisk(cpu1, cpu2, false)
            ),
            "⚠️ Risco Overclock" to listOf(
                "Probabilidade superaquecimento",
                "${getOverclockRisk(cpu1)}%",
                "${getOverclockRisk(cpu2)}%",
                compareThermalRisk(cpu1, cpu2, true)
            ),
            "🔥 Estabilidade Térmica" to listOf(
                "Avaliação geral",
                getThermalStabilityRating(cpu1),
                getThermalStabilityRating(cpu2),
                getBetterThermalChoice(cpu1, cpu2)
            )
        )
    }

    private fun calculateScore(cpu: Cpu): Int {
        return (cpu.turboClock * cpu.cores * cpu.threads * 1000).toInt()
    }

    private fun calculateSingleCoreScore(cpu: Cpu): Int {
        return (cpu.turboClock * 1000).toInt()
    }

    private fun calculateMultiCoreScore(cpu: Cpu): Int {
        return (cpu.turboClock * cpu.cores * 500).toInt()
    }

    private fun estimateFps(cpu: Cpu): Int {
        // Fórmula simplificada: FPS baseado em clock + núcleos (ajustável conforme dados reais)
        return (cpu.turboClock * cpu.cores * 10).toInt()
    }

    // NOVOS MÉTODOS PARA ANÁLISE TÉRMICA

    private fun estimateHeavyWorkloadTemp(cpu: Cpu): Int {
        val baseHeat = cpu.baseTemperature
        val tdpMultiplier = when {
            cpu.tdp > 150 -> 1.6
            cpu.tdp > 100 -> 1.4
            cpu.tdp > 65 -> 1.2
            else -> 1.1
        }

        val brandMultiplier = when {
            cpu.brand == "Intel" && isProblematicIntelGen(cpu.name) -> 1.5 // 13ª/14ª gen Intel
            cpu.brand == "Intel" -> 1.2
            else -> 1.0 // AMD geralmente mais frio
        }

        val estimatedTemp = (baseHeat * tdpMultiplier * brandMultiplier).toInt()

        return estimatedTemp.coerceAtMost(cpu.thermalThrottleTemp - 5)
    }

    private fun getOverclockRisk(cpu: Cpu): Int {

        val tempUnderLoad = estimateHeavyWorkloadTemp(cpu)
        val thermalHeadroom = cpu.thermalThrottleTemp - tempUnderLoad

        return when {
            thermalHeadroom < 10 -> 85
            thermalHeadroom < 20 -> 60
            thermalHeadroom < 30 -> 40
            thermalHeadroom < 40 -> 25
            else -> 15
        }

        val baseDanger = when {
            cpu.tdp > 150 -> 75
            cpu.tdp > 125 -> 60
            cpu.tdp > 100 -> 45
            cpu.tdp > 65 -> 30
            else -> 15
        }

        val brandRisk = when {
            cpu.brand == "Intel" && isProblematicIntelGen(cpu.name) -> 25 // +25% risco para Intel problemáticas
            cpu.brand == "Intel" -> 15 // +15% risco para Intel em geral
            cpu.name.contains("X3D") -> -10 // X3D da AMD são mais temperados
            else -> 0
        }

        return (baseDanger + brandRisk).coerceIn(5, 95)
    }

    private fun getThermalStabilityRating(cpu: Cpu): String {
        val temp = estimateHeavyWorkloadTemp(cpu)
        val riskLevel = getOverclockRisk(cpu)

        return when {
            temp > 85 || riskLevel > 70 -> "❌ Problemático"
            temp > 75 || riskLevel > 50 -> "⚠️ Requer atenção"
            temp > 65 || riskLevel > 30 -> "✅ Bom"
            else -> "🏆 Excelente"
        }
    }

    private fun compareThermalRisk(cpu1: Cpu, cpu2: Cpu, isOverclock: Boolean): String {
        val risk1 = if (isOverclock) getOverclockRisk(cpu1) else estimateHeavyWorkloadTemp(cpu1)
        val risk2 = if (isOverclock) getOverclockRisk(cpu2) else estimateHeavyWorkloadTemp(cpu2)

        return when {
            risk1 > risk2 -> "❄️ +${risk1 - risk2}${if (isOverclock) "%" else "°C"}"
            risk2 > risk1 -> "🔥 -${risk2 - risk1}${if (isOverclock) "%" else "°C"}"
            else -> "≈ Equivalente"
        }
    }

    private fun getBetterThermalChoice(cpu1: Cpu, cpu2: Cpu): String {
        val score1 = calculateThermalScore(cpu1)
        val score2 = calculateThermalScore(cpu2)

        return when {
            score1 > score2 + 10 -> "🏆 ${cpu1.name.take(10)}..."
            score2 > score1 + 10 -> "🏆 ${cpu2.name.take(10)}..."
            else -> "≈ Equivalente"
        }
    }

    private fun calculateThermalScore(cpu: Cpu): Int {
        // Pontuação baseada em temperatura + estabilidade
        val tempScore = (100 - estimateHeavyWorkloadTemp(cpu)).coerceAtLeast(0)
        val riskScore = (100 - getOverclockRisk(cpu))
        return (tempScore + riskScore) / 2
    }

    private fun isProblematicIntelGen(cpuName: String): Boolean {
        // Lista de CPUs Intel da 13ª e 14ª geração que tiveram problemas
        val problematicModels = listOf("13", "14", "i9-13", "i7-13", "i5-13", "i9-14", "i7-14", "i5-14")
        return problematicModels.any { cpuName.contains(it, ignoreCase = true) }
    }

    private fun getDifference(value1: Int, value2: Int): String {
        val diff = ((value1 - value2).toDouble() / value2 * 100).toInt()
        return if (diff > 0) "+$diff" else "$diff"
    }
}
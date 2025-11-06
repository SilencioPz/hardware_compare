package com.silenciopz.hardware.utils

import com.silenciopz.hardware.data.Gpu

class BenchmarkGpus {

    fun compareGpus(gpu1: Gpu, gpu2: Gpu): Map<String, List<String>> {
        return mapOf(
            "🎮 Performance Geral" to listOf(
                "Overall Score",
                "${calculateScore(gpu1)}",
                "${calculateScore(gpu2)}",
                "${getDifference(calculateScore(gpu1), calculateScore(gpu2))}%"
            ),
            "💾 VRAM" to listOf(
                "Memória de Vídeo",
                "${gpu1.memory} GB",
                "${gpu2.memory} GB",
                if (gpu1.memory > gpu2.memory) "+${gpu1.memory - gpu2.memory}GB" else "-${gpu2.memory - gpu1.memory}GB"
            ),
            "⚡ Clock Boost" to listOf(
                "Frequência Máxima",
                "${gpu1.turboClock} MHz",
                "${gpu2.turboClock} MHz",
                if (gpu1.turboClock > gpu2.turboClock) "+${gpu1.turboClock - gpu2.turboClock}MHz" else "-${gpu2.turboClock - gpu1.turboClock}MHz"
            ),
            "🔋 Consumo Energético" to listOf(
                "TDP (Watts)",
                "${gpu1.tdp}W",
                "${gpu2.tdp}W",
                if (gpu1.tdp > gpu2.tdp) "+${gpu1.tdp - gpu2.tdp}W" else "-${gpu2.tdp - gpu1.tdp}W"
            ),
            "🌡️ Temperatura Base" to listOf(
                "Temperatura em Idle",
                "${gpu1.baseTemperature}°C",
                "${gpu2.baseTemperature}°C",
                compareThermalValues(gpu1.baseTemperature, gpu2.baseTemperature, "°C")
            ),
            "🔥 Temperatura Máxima Segura" to listOf(
                "Limite de Segurança",
                "${gpu1.maxSafeTemperature}°C",
                "${gpu2.maxSafeTemperature}°C",
                if (gpu1.maxSafeTemperature > gpu2.maxSafeTemperature) "+${gpu1.maxSafeTemperature - gpu2.maxSafeTemperature}°C" else "-${gpu2.maxSafeTemperature - gpu1.maxSafeTemperature}°C"
            ),
            "⚠️ Thermal Throttling" to listOf(
                "Início do Throttling",
                "${gpu1.thermalThrottleTemp}°C",
                "${gpu2.thermalThrottleTemp}°C",
                if (gpu1.thermalThrottleTemp > gpu2.thermalThrottleTemp) "+${gpu1.thermalThrottleTemp - gpu2.thermalThrottleTemp}°C" else "-${gpu2.thermalThrottleTemp - gpu1.thermalThrottleTemp}°C"
            ),
            "❄️ Resfriamento Recomendado" to listOf(
                "Tipo de Cooler",
                gpu1.recommendedCooling,
                gpu2.recommendedCooling,
                compareCoolingRequirements(gpu1, gpu2)
            ),
            "⚡ Performance por Watt" to listOf(
                "Eficiência Energética",
                "${calculatePerformancePerWatt(gpu1)} pts/W",
                "${calculatePerformancePerWatt(gpu2)} pts/W",
                "${getDifference(calculatePerformancePerWatt(gpu1), calculatePerformancePerWatt(gpu2))}%"
            ),
            "🏆 Eficiência Térmica" to listOf(
                "Avaliação Térmica",
                getThermalEfficiencyRating(gpu1),
                getThermalEfficiencyRating(gpu2),
                getBetterThermalChoice(gpu1, gpu2)
            )
        )
    }

    private fun calculateScore(gpu: Gpu): Int {
        // Fórmula: VRAM * 1000 + Clock Boost * 10 + TDP (inverso) * 5
        return (gpu.memory * 1000) + (gpu.turboClock / 100) + ((250 - gpu.tdp.coerceAtMost(250)) * 5)
    }

    private fun calculatePerformancePerWatt(gpu: Gpu): Int {
        val score = calculateScore(gpu)
        return if (gpu.tdp > 0) score / gpu.tdp else 0
    }

    private fun getThermalEfficiencyRating(gpu: Gpu): String {
        val thermalGap = gpu.maxSafeTemperature - gpu.baseTemperature
        val thermalStability = when {
            thermalGap > 40 -> "🏆 Excelente"
            thermalGap > 30 -> "✅ Boa"
            thermalGap > 20 -> "⚠️ Regular"
            else -> "❌ Problemática"
        }

        val coolingRequirement = when {
            gpu.tdp > 300 -> " (Requer WC)"
            gpu.tdp > 200 -> " (Requer Tri-Fan)"
            else -> ""
        }

        return "$thermalStability$coolingRequirement"
    }

    private fun compareThermalValues(temp1: Int, temp2: Int, unit: String): String {
        return when {
            temp1 > temp2 -> "🔥 +${temp1 - temp2}$unit"
            temp2 > temp1 -> "❄️ -${temp2 - temp1}$unit"
            else -> "≈ Igual"
        }
    }

    private fun compareFpsValues(fps1: Int?, fps2: Int?): String {
        if (fps1 == null || fps2 == null) return "N/A"
        return when {
            fps1 > fps2 -> "🎯 +${fps1 - fps2}FPS"
            fps2 > fps1 -> "📉 -${fps2 - fps1}FPS"
            else -> "≈ Igual"
        }
    }

    private fun compareCoolingRequirements(gpu1: Gpu, gpu2: Gpu): String {
        val coolingLevel1 = getCoolingLevel(gpu1)
        val coolingLevel2 = getCoolingLevel(gpu2)

        return when {
            coolingLevel1 > coolingLevel2 -> "❄️ +${coolingLevel1 - coolingLevel2}nível"
            coolingLevel2 > coolingLevel1 -> "💨 -${coolingLevel2 - coolingLevel1}nível"
            else -> "≈ Similar"
        }
    }

    private fun getCoolingLevel(gpu: Gpu): Int {
        return when {
            gpu.tdp > 300 -> 4 // Water Cooling
            gpu.tdp > 200 -> 3 // Tri-Fan
            gpu.tdp > 150 -> 2 // Dual-Fan qualidade
            else -> 1 // Dual-Fan padrão
        }
    }

    private fun getBetterThermalChoice(gpu1: Gpu, gpu2: Gpu): String {
        val score1 = calculateThermalScore(gpu1)
        val score2 = calculateThermalScore(gpu2)

        return when {
            score1 > score2 + 15 -> "🏆 ${gpu1.name.take(12)}..."
            score2 > score1 + 15 -> "🏆 ${gpu2.name.take(12)}..."
            else -> "≈ Equivalente"
        }
    }

    private fun estimateGamingTemp(gpu: Gpu): Int {
        val baseTemp = gpu.baseTemperature
        val loadIncrease = when {
            gpu.tdp > 300 -> (gpu.tdp * 0.55).toInt() // GPUs high-end
            gpu.tdp > 200 -> (gpu.tdp * 0.45).toInt() // GPUs mid-high
            gpu.tdp > 150 -> (gpu.tdp * 0.35).toInt() // GPUs mid-range
            else -> (gpu.tdp * 0.25).toInt() // GPUs entry-level
        }

        val estimatedTemp = baseTemp + loadIncrease

        return estimatedTemp.coerceAtMost(gpu.maxSafeTemperature - 5)
    }

    private fun calculateThermalScore(gpu: Gpu): Int {
        val gamingTemp = estimateGamingTemp(gpu)
        val thermalHeadroom = gpu.thermalThrottleTemp - gamingTemp
        val tdpEfficiency = (250 - gpu.tdp.coerceAtMost(250)) * 2

        return (thermalHeadroom * 3) + tdpEfficiency
    }

    private fun getDifference(value1: Int, value2: Int): String {
        if (value2 == 0) return "N/A"
        val diff = ((value1 - value2).toDouble() / value2 * 100).toInt()
        return if (diff > 0) "+$diff" else "$diff"
    }
}
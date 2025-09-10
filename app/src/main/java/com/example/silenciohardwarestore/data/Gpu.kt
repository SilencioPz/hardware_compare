package com.example.silenciohardwarestore.data

data class Gpu(
    val id: Int,
    val name: String,
    val brand: String,  // "NVIDIA" ou "AMD"
    val memory: Int,  // GB
    val baseClock: Int,  // MHz
    val turboClock: Int,
    val effectiveClock: Int,
    val tdp: Int,  // Watts
    val coolingFans: Int,
    val caseSlots: Int,
    val baseTemperature: Int = calculateBaseTemp(brand, tdp, memory), // °C em idle
    val maxSafeTemperature: Int = calculateMaxSafeTemp(brand), // °C máxima segura
    val thermalThrottleTemp: Int = calculateThrottleTemp(brand), // °C onde começa throttling
    val recommendedCooling: String = calculateRecommendedCooling(tdp, memory),
    val score: Int = calculateGpuScore(name, brand, effectiveClock, memory)
) {
    companion object {
        private fun calculateBaseTemp(brand: String, tdp: Int, memory: Int): Int {
            return when {
                brand == "NVIDIA" && tdp > 250 -> 38 + (tdp / 15) // NVIDIA high-end (RTX 4090, etc)
                brand == "NVIDIA" -> 32 + (tdp / 20) // NVIDIA normal
                brand == "AMD" && memory >= 16 -> 36 + (tdp / 12) // AMD high-end (RX 7900 XTX, etc)
                else -> 30 + (tdp / 18) // AMD normal
            }.coerceIn(30, 45)
        }

        private fun calculateMaxSafeTemp(brand: String): Int {
            return when {
                brand == "NVIDIA" -> 83
                brand == "AMD" -> 85
                else -> 80
            }
        }

        private fun calculateThrottleTemp(brand: String): Int {
            return when {
                brand == "NVIDIA" -> 88
                brand == "AMD" -> 90
                else -> 85
            }
        }

        private fun calculateRecommendedCooling(tdp: Int, memory: Int): String {
            return when {
                tdp > 300 -> "Water Cooling Recomendado (AIO ou Custom Loop)"
                tdp > 200 -> "Cooler Tri-Fan ou Water Cooling"
                tdp > 150 -> "Cooler Dual-Fan de qualidade"
                else -> "Cooler Dual-Fan padrão"
            }
        }

        private fun calculateGpuScore(
            name: String,
            brand: String,
            effectiveClock: Int,
            memory: Int
        ): Int {
            val baseScore = (effectiveClock * 0.1f + memory * 500).toInt()

            val brandBonus = when (brand) {
                "NVIDIA" -> when {
                    name.contains("RTX 50", ignoreCase = true) -> 1.8f
                    name.contains("RTX 40", ignoreCase = true) -> 1.5f
                    name.contains("RTX 30", ignoreCase = true) -> 1.2f
                    name.contains("RTX 20", ignoreCase = true) -> 1.0f
                    name.contains("GTX 16", ignoreCase = true) -> 0.7f
                    name.contains("GTX", ignoreCase = true) -> 0.6f
                    else -> 1.0f
                }
                "AMD" -> when {
                    name.contains("RX 7", ignoreCase = true) -> 1.4f
                    name.contains("RX 6", ignoreCase = true) -> 1.1f
                    name.contains("RX 5", ignoreCase = true) -> 0.9f
                    else -> 1.0f
                }
                else -> 1.0f
            }

            return (baseScore * brandBonus).toInt()
        }
    }
}
package com.silenciopz.hardware.data

data class Cpu(
    val id: Int,
    val name: String,
    val brand: String,  // "AMD" ou "Intel"
    val socket: String,
    val baseClock: Double,  // GHz
    val turboClock: Double,
    val cores: Int,
    val threads: Int,
    val tdp: Int,  // Watts
    val maxRAM: Int,
    val L1Cache: String,
    val L2Cache: String,
    val L3Cache: String,
    val ECCSupported: String,
    val MultithreadingSupported: String,
    val baseTemperature: Int = calculateBaseTemp(brand, tdp, turboClock), // °C em idle
    val maxSafeTemperature: Int = calculateMaxSafeTemp(brand), // °C máxima segura
    val thermalThrottleTemp: Int = calculateThrottleTemp(brand), // °C onde começa throttling
    val score: Int = calculateCpuScore(name, brand, baseClock, turboClock, cores)
) {
    companion object {
        private fun calculateBaseTemp(brand: String, tdp: Int, turboClock: Double): Int {
            return when {
                brand.contains("Intel", ignoreCase = true) && tdp > 100 -> 38 + (tdp / 12)
                brand.contains("Intel", ignoreCase = true) -> 32 + (tdp / 18)
                brand.contains("AMD", ignoreCase = true) && turboClock > 4.5 -> 36 + (tdp / 15)
                else -> 30 + (tdp / 20)
            }.coerceIn(30, 50)
        }

        private fun calculateMaxSafeTemp(brand: String): Int {
            return when {
                brand.contains("Intel", ignoreCase = true) -> 100
                brand.contains("AMD", ignoreCase = true) -> 95
                else -> 85
            }
        }

        private fun calculateThrottleTemp(brand: String): Int {
            return when {
                brand.contains("Intel", ignoreCase = true) -> 105
                brand.contains("AMD", ignoreCase = true) -> 90
                else -> 85
            }
        }

        private fun calculateCpuScore(
            name: String,
            brand: String,
            baseClock: Double,
            turboClock: Double,
            cores: Int
        ): Int {
            val clockToUse = if (turboClock > 0) turboClock else baseClock
            var baseScore = (clockToUse * 1000).toInt()

            val coreBonus = when (cores) {
                in 1..2 -> 0.6f
                in 3..4 -> 0.8f
                in 5..6 -> 1.0f
                in 7..8 -> 1.2f
                in 9..12 -> 1.4f
                in 13..16 -> 1.6f
                else -> 1.8f
            }

            val brandBonus = when (brand) {
                "AMD" -> when {
                    name.contains("Ryzen 9", ignoreCase = true) -> 1.8f
                    name.contains("Ryzen 7", ignoreCase = true) -> 1.4f
                    name.contains("Ryzen 5", ignoreCase = true) -> 1.1f
                    name.contains("Ryzen 3", ignoreCase = true) -> 0.9f
                    else -> 1.0f
                }
                "Intel" -> when {
                    name.contains("i9", ignoreCase = true) -> 1.8f
                    name.contains("i7", ignoreCase = true) -> 1.4f
                    name.contains("i5", ignoreCase = true) -> 1.1f
                    name.contains("i3", ignoreCase = true) -> 0.9f
                    else -> 1.0f
                }
                else -> 1.0f
            }

            return (baseScore * coreBonus * brandBonus).toInt()
        }
    }
}
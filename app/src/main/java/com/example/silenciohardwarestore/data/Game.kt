package com.example.silenciohardwarestore.data

data class Game(
    val id: Int,
    val name: String,
    val genre: String,
    val recommendedCpu: String,
    val recommendedGpu: String,
    val bottleneckMultiplier: Float = 1.0f,
    val cpuIntensity: Float = 0.7f, // 0-1 scale (CPU dependency)
    val gpuIntensity: Float = 0.7f,  // 0-1 scale (GPU dependency)
    val minCpuScore: Int = calculateMinCpuScore(bottleneckMultiplier),
    val recCpuScore: Int = calculateRecCpuScore(bottleneckMultiplier),
    val minGpuScore: Int = calculateMinGpuScore(bottleneckMultiplier),
    val recGpuScore: Int = calculateRecGpuScore(bottleneckMultiplier)
) {
    companion object {
        private fun calculateMinCpuScore(bottleneckMultiplier: Float): Int {
            return (15000 * bottleneckMultiplier).toInt()
        }

        private fun calculateRecCpuScore(bottleneckMultiplier: Float): Int {
            return (25000 * bottleneckMultiplier).toInt()
        }

        private fun calculateMinGpuScore(bottleneckMultiplier: Float): Int {
            return (8000 * bottleneckMultiplier).toInt()
        }

        private fun calculateRecGpuScore(bottleneckMultiplier: Float): Int {
            return (15000 * bottleneckMultiplier).toInt()
        }
    }
}
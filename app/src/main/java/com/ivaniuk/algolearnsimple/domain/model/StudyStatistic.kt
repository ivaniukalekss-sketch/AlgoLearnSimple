package com.ivaniuk.algolearnsimple.domain.model

import java.time.LocalDateTime
import java.time.Duration
import java.util.UUID

data class StudyStatistic(
    val id: String = UUID.randomUUID().toString(),
    val algorithmId: Int,
    val algorithmName: String,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val duration: Long, // в миллисекундах
    val stepsCompleted: Int,
    val totalSteps: Int,
    val visitedAllSteps: Boolean = false
) {
    val completionPercentage: Float
        get() = if (totalSteps > 0) {
            (stepsCompleted.toFloat() / totalSteps.toFloat()) * 100
        } else {
            0f
        }

    fun getFormattedDuration(): String {
        val durationObj = Duration.ofMillis(duration)
        val hours = durationObj.toHours()
        val minutes = durationObj.toMinutes() % 60

        return when {
            hours > 0 -> "${hours}ч ${minutes}м"
            minutes > 0 -> "${minutes}м"
            else -> "< 1м"
        }
    }
}

data class UserStatistics(
    val totalStudyTime: Long = 0, // в миллисекундах
    val totalSessions: Int = 0,
    val algorithmsViewed: Set<Int> = emptySet(),
    val algorithmStats: Map<Int, AlgorithmStatistic> = emptyMap(),
    val dailyStats: Map<String, DailyStatistic> = emptyMap() // Ключ: "yyyy-MM-dd"
) {
    val algorithmsLearned: Int
        get() = algorithmStats.count { (_, stat) -> stat.completionRate >= 80 }

    val favoriteAlgorithm: Pair<Int, String>?
        get() = algorithmStats.maxByOrNull { (_, stat) -> stat.totalTime }
            ?.let { (id, stat) -> id to stat.algorithmName }

    val averageSessionTime: Long
        get() = if (totalSessions > 0) totalStudyTime / totalSessions else 0
}

data class AlgorithmStatistic(
    val algorithmId: Int,
    val algorithmName: String,
    val totalViews: Int = 0,
    val totalTime: Long = 0, // в миллисекундах
    val timesCompleted: Int = 0,
    val lastViewed: LocalDateTime? = null,
    val completionRate: Float = 0f
)

data class DailyStatistic(
    val date: String, // Формат: "yyyy-MM-dd"
    val studyTime: Long = 0,
    val sessions: Int = 0,
    val algorithmsStudied: Set<Int> = emptySet()
)
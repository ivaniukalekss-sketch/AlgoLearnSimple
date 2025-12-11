package com.ivaniuk.algolearnsimple.domain.repository

import com.ivaniuk.algolearnsimple.domain.model.*
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
    // Сохранение статистики
    suspend fun saveStudySession(statistic: StudyStatistic)
    suspend fun incrementAlgorithmView(algorithmId: Int, algorithmName: String)

    // Получение статистики
    fun getUserStatistics(): Flow<UserStatistics>
    fun getAlgorithmStatistics(algorithmId: Int): Flow<AlgorithmStatistic>
    fun getRecentSessions(limit: Int = 10): Flow<List<StudyStatistic>>
    fun getWeeklyStats(): Flow<Map<String, DailyStatistic>> // Последние 7 дней
    fun getMonthlyStats(): Flow<Map<String, DailyStatistic>> // Последние 30 дней

    // Агрегированные данные
    fun getTotalStudyTime(): Flow<Long>
    fun getMostStudiedAlgorithm(): Flow<Pair<Int, String>?>
    fun getLearningStreak(): Flow<Int> // Количество дней подряд

    // Сброс статистики
    suspend fun resetStatistics()
}
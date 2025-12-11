package com.ivaniuk.algolearnsimple.data.repository

import android.content.Context
import com.ivaniuk.algolearnsimple.data.local.LocalStorage
import com.ivaniuk.algolearnsimple.domain.model.*
import com.ivaniuk.algolearnsimple.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StatisticsRepositoryImpl(
    private val context: Context,
    private val localStorage: LocalStorage
) : StatisticsRepository {

    // Ключи для SharedPreferences
    private companion object {
        const val KEY_STUDY_STATS = "study_statistics"
        const val KEY_USER_STATS = "user_statistics"
        const val KEY_DAILY_STATS = "daily_statistics"
        const val KEY_LEARNING_STREAK = "learning_streak"
        const val KEY_LAST_STUDY_DATE = "last_study_date"
    }

    private val _studyStats = MutableStateFlow<List<StudyStatistic>>(emptyList())
    private val _userStats = MutableStateFlow(UserStatistics())
    private val _dailyStats = MutableStateFlow<Map<String, DailyStatistic>>(emptyMap())

    init {
        loadFromStorage()
    }

    private fun loadFromStorage() {
        // Загрузка из SharedPreferences (упрощённо)
        // В реальном приложении можно использовать Room Database
    }

    private fun saveToStorage() {
        // Сохранение в SharedPreferences
    }

    private fun getCurrentDateKey(): String {
        return LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    }

    private fun updateLearningStreak() {
        val lastDate = localStorage.getString(KEY_LAST_STUDY_DATE)
        val currentDate = LocalDate.now().toString()

        if (lastDate != currentDate) {
            val lastStudyDate = lastDate?.let { LocalDate.parse(it) }
            val yesterday = LocalDate.now().minusDays(1)

            val currentStreak = localStorage.getInt(KEY_LEARNING_STREAK, 0)

            if (lastStudyDate == yesterday) {
                // Пользователь учился вчера - увеличиваем стрик
                localStorage.saveInt(KEY_LEARNING_STREAK, currentStreak + 1)
            } else if (lastStudyDate != LocalDate.now()) {
                // Пропущен день - сбрасываем стрик
                localStorage.saveInt(KEY_LEARNING_STREAK, 1)
            }

            localStorage.saveString(KEY_LAST_STUDY_DATE, currentDate)
        }
    }

    override suspend fun saveStudySession(statistic: StudyStatistic) {
        _studyStats.update { current ->
            (current + statistic).sortedByDescending { it.dateTime }
        }

        // Обновляем статистику пользователя
        _userStats.update { current ->
            val algorithmStats = current.algorithmStats.toMutableMap()
            val currentAlgStat = algorithmStats[statistic.algorithmId]
                ?: AlgorithmStatistic(
                    algorithmId = statistic.algorithmId,
                    algorithmName = statistic.algorithmName
                )

            val updatedAlgStat = currentAlgStat.copy(
                totalViews = currentAlgStat.totalViews + 1,
                totalTime = currentAlgStat.totalTime + statistic.duration,
                timesCompleted = if (statistic.visitedAllSteps) {
                    currentAlgStat.timesCompleted + 1
                } else {
                    currentAlgStat.timesCompleted
                },
                lastViewed = LocalDateTime.now(),
                completionRate = ((currentAlgStat.completionRate + statistic.completionPercentage) / 2)
            )

            algorithmStats[statistic.algorithmId] = updatedAlgStat

            // Обновляем дневную статистику
            val dateKey = getCurrentDateKey()
            val currentDailyStat = _dailyStats.value[dateKey]
                ?: DailyStatistic(date = dateKey)

            val updatedDailyStat = currentDailyStat.copy(
                studyTime = currentDailyStat.studyTime + statistic.duration,
                sessions = currentDailyStat.sessions + 1,
                algorithmsStudied = currentDailyStat.algorithmsStudied + statistic.algorithmId
            )

            _dailyStats.update { currentDaily ->
                currentDaily.toMutableMap().apply {
                    put(dateKey, updatedDailyStat)
                }
            }

            updateLearningStreak()

            current.copy(
                totalStudyTime = current.totalStudyTime + statistic.duration,
                totalSessions = current.totalSessions + 1,
                algorithmsViewed = current.algorithmsViewed + statistic.algorithmId,
                algorithmStats = algorithmStats,
                dailyStats = _dailyStats.value
            )
        }

        saveToStorage()
    }

    override suspend fun incrementAlgorithmView(algorithmId: Int, algorithmName: String) {
        _userStats.update { current ->
            val algorithmStats = current.algorithmStats.toMutableMap()
            val currentStat = algorithmStats[algorithmId]
                ?: AlgorithmStatistic(algorithmId = algorithmId, algorithmName = algorithmName)

            algorithmStats[algorithmId] = currentStat.copy(
                totalViews = currentStat.totalViews + 1,
                lastViewed = LocalDateTime.now()
            )

            current.copy(algorithmStats = algorithmStats)
        }
    }

    override fun getUserStatistics(): Flow<UserStatistics> = _userStats

    override fun getAlgorithmStatistics(algorithmId: Int): Flow<AlgorithmStatistic> =
        _userStats.map { userStats ->
            userStats.algorithmStats[algorithmId] ?: AlgorithmStatistic(
                algorithmId = algorithmId,
                algorithmName = ""
            )
        }

    override fun getRecentSessions(limit: Int): Flow<List<StudyStatistic>> =
        _studyStats.map { stats ->
            stats.take(limit)
        }

    override fun getWeeklyStats(): Flow<Map<String, DailyStatistic>> {
        return _dailyStats.map { dailyStats ->
            val weekAgo = LocalDate.now().minusDays(7)
            dailyStats.filter { (dateKey, _) ->
                LocalDate.parse(dateKey).isAfter(weekAgo) ||
                        LocalDate.parse(dateKey).isEqual(weekAgo)
            }
        }
    }

    override fun getMonthlyStats(): Flow<Map<String, DailyStatistic>> {
        return _dailyStats.map { dailyStats ->
            val monthAgo = LocalDate.now().minusDays(30)
            dailyStats.filter { (dateKey, _) ->
                LocalDate.parse(dateKey).isAfter(monthAgo) ||
                        LocalDate.parse(dateKey).isEqual(monthAgo)
            }
        }
    }

    override fun getTotalStudyTime(): Flow<Long> =
        _userStats.map { it.totalStudyTime }

    override fun getMostStudiedAlgorithm(): Flow<Pair<Int, String>?> =
        _userStats.map { it.favoriteAlgorithm }

    override fun getLearningStreak(): Flow<Int> =
        MutableStateFlow(localStorage.getInt(KEY_LEARNING_STREAK, 0))

    override suspend fun resetStatistics() {
        _studyStats.value = emptyList()
        _userStats.value = UserStatistics()
        _dailyStats.value = emptyMap()
        localStorage.clear()
        saveToStorage()
    }
}
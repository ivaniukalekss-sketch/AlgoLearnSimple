package com.ivaniuk.algolearnsimple.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivaniuk.algolearnsimple.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val statisticsRepository: StatisticsRepository
) : ViewModel() {

    // Основная статистика пользователя
    val userStatistics = statisticsRepository.getUserStatistics()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Недавние сессии
    val recentSessions = statisticsRepository.getRecentSessions(5)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Недельная статистика
    val weeklyStats = statisticsRepository.getWeeklyStats()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    // Серия обучения (стрик)
    val learningStreak = statisticsRepository.getLearningStreak()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Самый изучаемый алгоритм
    val mostStudiedAlgorithm = statisticsRepository.getMostStudiedAlgorithm()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Запись новой сессии изучения
    fun saveStudySession(
        algorithmId: Int,
        algorithmName: String,
        duration: Long,
        stepsCompleted: Int,
        totalSteps: Int,
        visitedAllSteps: Boolean = false
    ) {
        viewModelScope.launch {
            statisticsRepository.saveStudySession(
                com.ivaniuk.algolearnsimple.domain.model.StudyStatistic(
                    algorithmId = algorithmId,
                    algorithmName = algorithmName,
                    duration = duration,
                    stepsCompleted = stepsCompleted,
                    totalSteps = totalSteps,
                    visitedAllSteps = visitedAllSteps
                )
            )
        }
    }

    // Увеличение просмотра алгоритма
    fun incrementAlgorithmView(algorithmId: Int, algorithmName: String) {
        viewModelScope.launch {
            statisticsRepository.incrementAlgorithmView(algorithmId, algorithmName)
        }
    }

    // Сброс статистики
    fun resetStatistics() {
        viewModelScope.launch {
            statisticsRepository.resetStatistics()
        }
    }

    // Получение статистики для алгоритма
    fun getAlgorithmStatistics(algorithmId: Int): StateFlow<com.ivaniuk.algolearnsimple.domain.model.AlgorithmStatistic?> {
        return statisticsRepository.getAlgorithmStatistics(algorithmId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    }
}
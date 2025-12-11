package com.ivaniuk.algolearnsimple.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivaniuk.algolearnsimple.domain.model.Speed
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.model.StudyStatistic
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import com.ivaniuk.algolearnsimple.domain.repository.StatisticsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class VisualizationViewModel(
    private val visualizer: AlgorithmVisualizer,
    private val statisticsRepository: StatisticsRepository? = null
) : ViewModel() {

    // Статистические данные
    private var startTime: Long = 0
    private var totalStepsVisited: Int = 0
    private var maxStepReached: Int = 0
    private var visitedSteps: MutableSet<Int> = mutableSetOf()

    // Основные состояния визуализации (остаются без изменений)
    private val _steps = MutableStateFlow<List<VisualizationStep>>(emptyList())
    val steps: StateFlow<List<VisualizationStep>> = _steps.asStateFlow()

    private val _currentStepIndex = MutableStateFlow(0)
    val currentStepIndex: StateFlow<Int> = _currentStepIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _speed = MutableStateFlow(Speed.MEDIUM)
    val speed: StateFlow<Speed> = _speed.asStateFlow()

    private var playbackJob: Job? = null

    // Новое состояние для отслеживания изменений шагов
    private val _stepChanges = MutableStateFlow(0)
    val stepChanges: StateFlow<Int> = _stepChanges.asStateFlow()

    init {
        startTime = System.currentTimeMillis()
        loadVisualization()
    }

    private fun loadVisualization() {
        viewModelScope.launch {
            visualizer.visualize(visualizer.getDefaultInput()).collect { stepsList ->
                _steps.value = stepsList
                resetToFirstStep()

                // Отслеживаем просмотр алгоритма
                statisticsRepository?.let {
                    viewModelScope.launch {
                        it.incrementAlgorithmView(
                            visualizer.algorithmId,
                            visualizer.algorithmName
                        )
                    }
                }
            }
        }
    }

    fun play() {
        if (_isPlaying.value) return

        _isPlaying.value = true
        playbackJob = viewModelScope.launch {
            val baseDelay = when (visualizer.algorithmName) {
                "Bubble Sort" -> _speed.value.delayMs * 2  // Для сортировки медленнее
                else -> _speed.value.delayMs
            }

            while (_currentStepIndex.value < _steps.value.size - 1 && _isPlaying.value) {
                delay(baseDelay)
                nextStep()
            }
            _isPlaying.value = false

            // После завершения воспроизложения сохраняем статистику
            if (_currentStepIndex.value >= _steps.value.size - 1) {
                saveStatistics(true) // Завершено полностью
            }
        }
    }

    fun pause() {
        _isPlaying.value = false
        playbackJob?.cancel()
        playbackJob = null
    }

    fun stop() {
        pause()
        resetToFirstStep()
        // При остановке сохраняем текущую статистику
        saveStatistics(false)
    }

    fun nextStep() {
        if (_currentStepIndex.value < _steps.value.size - 1) {
            _currentStepIndex.value++
            trackStepVisited()
            _stepChanges.value++
        } else {
            pause()
            saveStatistics(true) // Дошли до конца
        }
    }

    fun previousStep() {
        if (_currentStepIndex.value > 0) {
            _currentStepIndex.value--
            _stepChanges.value--
        }
    }

    fun setStep(index: Int) {
        if (index in 0 until _steps.value.size) {
            _currentStepIndex.value = index
            trackStepVisited()
            _stepChanges.value = index
        }
    }

    fun setSpeed(newSpeed: Speed) {
        _speed.value = newSpeed
        // Если воспроизведение идёт, перезапускаем с новой скоростью
        if (_isPlaying.value) {
            val currentIndex = _currentStepIndex.value
            pause()
            _currentStepIndex.value = currentIndex
            play()
        }
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            pause()
        } else {
            play()
        }
    }

    fun resetToFirstStep() {
        _currentStepIndex.value = 0
        _stepChanges.value = 0
        // Сброс статистики визитов при сбросе на начало
        visitedSteps.clear()
        totalStepsVisited = 0
        maxStepReached = 0
        startTime = System.currentTimeMillis() // Сбрасываем таймер
    }

    fun getCurrentStep(): VisualizationStep? {
        return _steps.value.getOrNull(_currentStepIndex.value)
    }

    fun getAlgorithmName(): String = visualizer.algorithmName

    fun getInputDescription(): String = visualizer.getInputDescription()

    // Новые методы для статистики
    private fun trackStepVisited() {
        val stepIndex = _currentStepIndex.value

        // Если шаг ещё не посещался
        if (!visitedSteps.contains(stepIndex)) {
            visitedSteps.add(stepIndex)
            totalStepsVisited++
        }

        // Обновляем максимальный достигнутый шаг
        if (stepIndex > maxStepReached) {
            maxStepReached = stepIndex
        }
    }

    private fun saveStatistics(completed: Boolean = false) {
        statisticsRepository?.let { repository ->
            viewModelScope.launch {
                val endTime = System.currentTimeMillis()
                val duration = endTime - startTime

                // Проверяем, посетили ли все шаги
                val visitedAllSteps = visitedSteps.size >= _steps.value.size ||
                        maxStepReached >= _steps.value.size - 1

                repository.saveStudySession(
                    StudyStatistic(
                        algorithmId = visualizer.algorithmId,
                        algorithmName = visualizer.algorithmName,
                        dateTime = LocalDateTime.now(),
                        duration = duration,
                        stepsCompleted = maxStepReached + 1, // +1 потому что индексы с 0
                        totalSteps = _steps.value.size,
                        visitedAllSteps = visitedAllSteps || completed
                    )
                )
            }
        }
    }

    fun saveCurrentProgress() {
        // Метод для ручного сохранения прогресса (например, при выходе с экрана)
        if (_currentStepIndex.value > 0 || visitedSteps.isNotEmpty()) {
            saveStatistics(false)
        }
    }

    // Метод для получения статистики текущей сессии
    fun getCurrentSessionStats(): Map<String, Any> {
        val duration = System.currentTimeMillis() - startTime
        val progressPercentage = if (_steps.value.isNotEmpty()) {
            ((maxStepReached + 1).toFloat() / _steps.value.size.toFloat()) * 100
        } else {
            0f
        }

        return mapOf(
            "duration" to duration,
            "stepsVisited" to visitedSteps.size,
            "totalSteps" to _steps.value.size,
            "progress" to progressPercentage,
            "maxStepReached" to maxStepReached,
            "isPlaying" to _isPlaying.value,
            "currentSpeed" to _speed.value
        )
    }

    override fun onCleared() {
        super.onCleared()
        playbackJob?.cancel()
        // Сохраняем статистику при уничтожении ViewModel
        saveCurrentProgress()
    }

    fun goToLastStep() {
        if (_steps.value.isNotEmpty()) {
            _currentStepIndex.value = _steps.value.size - 1
            trackStepVisited()
            _stepChanges.value = _steps.value.size - 1
            pause()
            saveStatistics(true)
        }
    }

    // Дополнительные методы для UI

    fun getProgressPercentage(): Float {
        return if (_steps.value.isNotEmpty()) {
            (_currentStepIndex.value.toFloat() / (_steps.value.size - 1).toFloat()) * 100
        } else {
            0f
        }
    }

    fun getFormattedTimeSpent(): String {
        val duration = System.currentTimeMillis() - startTime
        val minutes = (duration / (1000 * 60)).toInt()
        val seconds = (duration / 1000) % 60

        return if (minutes > 0) {
            "${minutes}м ${seconds}с"
        } else {
            "${seconds}с"
        }
    }

    fun hasVisitedStep(stepIndex: Int): Boolean {
        return visitedSteps.contains(stepIndex)
    }

    fun getVisitedStepsCount(): Int = visitedSteps.size
}
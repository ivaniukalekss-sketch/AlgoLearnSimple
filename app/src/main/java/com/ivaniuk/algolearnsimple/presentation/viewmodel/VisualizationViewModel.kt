package com.ivaniuk.algolearnsimple.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivaniuk.algolearnsimple.domain.model.Speed
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VisualizationViewModel(
    private val visualizer: AlgorithmVisualizer
) : ViewModel() {

    private val _steps = MutableStateFlow<List<VisualizationStep>>(emptyList())
    val steps: StateFlow<List<VisualizationStep>> = _steps.asStateFlow()

    private val _currentStepIndex = MutableStateFlow(0)
    val currentStepIndex: StateFlow<Int> = _currentStepIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _speed = MutableStateFlow(Speed.MEDIUM)
    val speed: StateFlow<Speed> = _speed.asStateFlow()

    private val _isRandomData = MutableStateFlow(false)
    val isRandomData: StateFlow<Boolean> = _isRandomData.asStateFlow()

    private var playbackJob: Job? = null

    init {
        loadDefaultVisualization()
    }

    private fun loadDefaultVisualization() {
        loadVisualization(visualizer.getDefaultInput())
        _isRandomData.value = false
    }

    private fun loadVisualization(input: Any) {
        viewModelScope.launch {
            visualizer.visualize(input).collect { stepsList ->
                _steps.value = stepsList
                resetToFirstStep()
            }
        }
    }

    fun generateRandomData() {
        try {
            val randomInput = visualizer.generateRandomInput()
            _isRandomData.value = true
            loadVisualization(randomInput)
        } catch (_: Exception) {
            _isRandomData.value = false
            loadDefaultVisualization()
        }
    }

    fun resetToDefaultData() {
        loadDefaultVisualization()
    }

    fun play() {
        if (_isPlaying.value) return

        _isPlaying.value = true
        playbackJob = viewModelScope.launch {
            val baseDelay = when (visualizer.algorithmName) {
                "Bubble Sort" -> _speed.value.delayMs * 2
                else -> _speed.value.delayMs
            }

            while (_currentStepIndex.value < _steps.value.size - 1 && _isPlaying.value) {
                delay(baseDelay)
                nextStep()
            }
            _isPlaying.value = false
        }
    }

    fun pause() {
        _isPlaying.value = false
        playbackJob?.cancel()
        playbackJob = null
    }


    fun nextStep() {
        if (_currentStepIndex.value < _steps.value.size - 1) {
            _currentStepIndex.value++
        } else {
            pause()
        }
    }

    fun previousStep() {
        if (_currentStepIndex.value > 0) {
            _currentStepIndex.value--
        }
    }

    fun setStep(index: Int) {
        if (index in 0 until _steps.value.size) {
            _currentStepIndex.value = index
        }
    }

    fun setSpeed(newSpeed: Speed) {
        _speed.value = newSpeed
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
    }

    fun getCurrentStep(): VisualizationStep? {
        return _steps.value.getOrNull(_currentStepIndex.value)
    }

    fun getAlgorithmName(): String = visualizer.algorithmName

    fun getInputDescription(): String = visualizer.getInputDescription()

    override fun onCleared() {
        super.onCleared()
        playbackJob?.cancel()
    }

    fun goToLastStep() {
        _currentStepIndex.value = _steps.value.size - 1
        pause()
    }
}
package com.ivaniuk.algolearnsimple.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import com.ivaniuk.algolearnsimple.domain.repository.StatisticsRepository

class HomeViewModelFactory(
    private val algorithmRepository: AlgorithmRepository,
    private val statisticsRepository: StatisticsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(algorithmRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class StatisticsViewModelFactory(
    private val statisticsRepository: StatisticsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            return StatisticsViewModel(statisticsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
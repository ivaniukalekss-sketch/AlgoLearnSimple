package com.ivaniuk.algolearnsimple

import android.content.Context
import com.ivaniuk.algolearnsimple.data.local.LocalStorage
import com.ivaniuk.algolearnsimple.data.repository.AlgorithmRepositoryImpl
import com.ivaniuk.algolearnsimple.data.repository.StatisticsRepositoryImpl
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import com.ivaniuk.algolearnsimple.domain.repository.StatisticsRepository
import com.ivaniuk.algolearnsimple.presentation.viewmodel.HomeViewModelFactory
import com.ivaniuk.algolearnsimple.presentation.viewmodel.StatisticsViewModelFactory

class AppContainer(private val context: Context) {
    private val localStorage = LocalStorage(context)

    val algorithmRepository: AlgorithmRepository = AlgorithmRepositoryImpl(context)
    val statisticsRepository: StatisticsRepository = StatisticsRepositoryImpl(context, localStorage)

    // Инициализация ViewModel
    fun provideHomeViewModelFactory(): HomeViewModelFactory {
        return HomeViewModelFactory(algorithmRepository, statisticsRepository)
    }

    fun provideStatisticsViewModelFactory(): StatisticsViewModelFactory {
        return StatisticsViewModelFactory(statisticsRepository)
    }
}
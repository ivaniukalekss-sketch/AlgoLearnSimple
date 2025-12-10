package com.ivaniuk.algolearnsimple

import android.content.Context
import com.ivaniuk.algolearnsimple.data.repository.AlgorithmRepositoryImpl
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository

class AppContainer(private val context: Context) {
    val algorithmRepository: AlgorithmRepository = AlgorithmRepositoryImpl(context)
}
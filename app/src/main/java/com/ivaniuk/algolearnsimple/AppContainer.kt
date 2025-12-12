package com.ivaniuk.algolearnsimple

import android.content.Context
import com.ivaniuk.algolearnsimple.data.local.LocalStorage
import com.ivaniuk.algolearnsimple.data.repository.AlgorithmRepositoryImpl
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository

class AppContainer(private val context: Context) {
    private val localStorage = LocalStorage(context)

    val algorithmRepository: AlgorithmRepository = AlgorithmRepositoryImpl(context)
}
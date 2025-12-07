package com.ivaniuk.algolearnsimple

import com.ivaniuk.algolearnsimple.data.repository.AlgorithmRepositoryImpl
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository

class AppContainer {
    val algorithmRepository: AlgorithmRepository = AlgorithmRepositoryImpl()
}
package com.ivaniuk.algolearnsimple.domain.usecase

import com.ivaniuk.algolearnsimple.data.local.LocalStorage
import javax.inject.Inject

class AddToHistoryUseCase @Inject constructor(
    private val localStorage: LocalStorage
) {
    suspend operator fun invoke(algorithmId: Int, algorithmTitle: String) {
        localStorage.addToHistory(algorithmId, algorithmTitle)
    }
}
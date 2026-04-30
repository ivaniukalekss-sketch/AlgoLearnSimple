package com.ivaniuk.algolearnsimple.domain.usecase

import com.ivaniuk.algolearnsimple.data.local.LocalStorage
import javax.inject.Inject

class ClearHistoryUseCase @Inject constructor(
    private val localStorage: LocalStorage
) {
    suspend operator fun invoke() {
        localStorage.clearHistory()
    }
}
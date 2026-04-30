package com.ivaniuk.algolearnsimple.domain.usecase

import com.ivaniuk.algolearnsimple.data.local.HistoryEntity
import com.ivaniuk.algolearnsimple.data.local.LocalStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentHistoryUseCase @Inject constructor(
    private val localStorage: LocalStorage
) {
    operator fun invoke(): Flow<List<HistoryEntity>> = localStorage.getRecentHistory()
}
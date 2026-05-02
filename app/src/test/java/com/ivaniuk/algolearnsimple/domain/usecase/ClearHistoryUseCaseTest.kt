package com.ivaniuk.algolearnsimple.domain.usecase

import com.ivaniuk.algolearnsimple.data.local.LocalStorage
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class ClearHistoryUseCaseTest {

    @Mock
    private lateinit var localStorage: LocalStorage

    private lateinit var clearHistoryUseCase: ClearHistoryUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        clearHistoryUseCase = ClearHistoryUseCase(localStorage)
    }

    @Test
    fun testInvokeCallsLocalStorageClearHistory() {
        runBlocking {
            clearHistoryUseCase()

            verify(localStorage).clearHistory()
        }
    }
}
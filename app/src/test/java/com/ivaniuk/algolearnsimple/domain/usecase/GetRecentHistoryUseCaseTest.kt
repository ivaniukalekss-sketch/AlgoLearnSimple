package com.ivaniuk.algolearnsimple.domain.usecase

import com.ivaniuk.algolearnsimple.data.local.HistoryEntity
import com.ivaniuk.algolearnsimple.data.local.LocalStorage
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class GetRecentHistoryUseCaseTest {

    @Mock
    private lateinit var localStorage: LocalStorage

    private lateinit var getRecentHistoryUseCase: GetRecentHistoryUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getRecentHistoryUseCase = GetRecentHistoryUseCase(localStorage)
    }

    @Test
    fun testInvokeReturnsRecentHistoryFromLocalStorage() {
        runBlocking {
            val expectedHistory = listOf(
                HistoryEntity(algorithmId = 1, algorithmTitle = "Bubble Sort", timestamp = 123456789)
            )
            `when`(localStorage.getRecentHistory()).thenReturn(flowOf(expectedHistory))

            val result = getRecentHistoryUseCase()

            result.collect { history ->
                assertEquals(expectedHistory, history)
            }
            verify(localStorage).getRecentHistory()
        }
    }
}
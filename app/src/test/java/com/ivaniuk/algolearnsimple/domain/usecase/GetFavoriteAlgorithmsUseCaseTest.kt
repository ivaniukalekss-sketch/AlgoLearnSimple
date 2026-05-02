package com.ivaniuk.algolearnsimple.domain.usecase

import com.ivaniuk.algolearnsimple.domain.model.Algorithm
import com.ivaniuk.algolearnsimple.domain.model.AlgorithmCategory
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class GetFavoriteAlgorithmsUseCaseTest {

    @Mock
    private lateinit var repository: AlgorithmRepository

    private lateinit var getFavoriteAlgorithmsUseCase: GetFavoriteAlgorithmsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getFavoriteAlgorithmsUseCase = GetFavoriteAlgorithmsUseCase(repository)
    }

    @Test
    fun testInvokeReturnsFavoriteAlgorithmsFromRepository() {
        runBlocking {
            val expectedAlgorithms = listOf(
                Algorithm(1, "Bubble Sort", "", AlgorithmCategory.SORTING, "O(n²)", "", emptyList(), true)
            )
            `when`(repository.getFavoriteAlgorithms()).thenReturn(flowOf(expectedAlgorithms))

            val result = getFavoriteAlgorithmsUseCase()

            result.collect { algorithms ->
                assertEquals(expectedAlgorithms, algorithms)
            }
            verify(repository).getFavoriteAlgorithms()
        }
    }
}
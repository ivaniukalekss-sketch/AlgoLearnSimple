package com.ivaniuk.algolearnsimple.domain.usecase

import com.ivaniuk.algolearnsimple.domain.model.Algorithm
import com.ivaniuk.algolearnsimple.domain.model.AlgorithmCategory
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class GetAlgorithmByIdUseCaseTest {

    @Mock
    private lateinit var repository: AlgorithmRepository

    private lateinit var getAlgorithmByIdUseCase: GetAlgorithmByIdUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getAlgorithmByIdUseCase = GetAlgorithmByIdUseCase(repository)
    }

    @Test
    fun testInvokeReturnsAlgorithmFromRepository() {
        runBlocking {
            val algorithmId = 1
            val expectedAlgorithm = Algorithm(algorithmId, "Bubble Sort", "", AlgorithmCategory.SORTING, "O(n²)", "", emptyList(), false)
            `when`(repository.getAlgorithmById(algorithmId)).thenReturn(expectedAlgorithm)

            val result = getAlgorithmByIdUseCase(algorithmId)

            assertEquals(expectedAlgorithm, result)
            verify(repository).getAlgorithmById(algorithmId)
        }
    }

    @Test
    fun testInvokeReturnsNullWhenAlgorithmNotFound() {
        runBlocking {
            val algorithmId = 999
            `when`(repository.getAlgorithmById(algorithmId)).thenReturn(null)

            val result = getAlgorithmByIdUseCase(algorithmId)

            assertNull(result)
        }
    }
}
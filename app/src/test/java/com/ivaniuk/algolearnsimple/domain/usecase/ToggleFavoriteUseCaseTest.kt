package com.ivaniuk.algolearnsimple.domain.usecase

import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class ToggleFavoriteUseCaseTest {

    @Mock
    private lateinit var repository: AlgorithmRepository

    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        toggleFavoriteUseCase = ToggleFavoriteUseCase(repository)
    }

    @Test
    fun testInvokeCallsRepositoryToggleFavorite() {
        runBlocking {
            val algorithmId = 1

            toggleFavoriteUseCase(algorithmId)

            verify(repository).toggleFavorite(algorithmId)
        }
    }
}
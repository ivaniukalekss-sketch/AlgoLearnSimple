package com.ivaniuk.algolearnsimple.presentation.viewmodel

import com.ivaniuk.algolearnsimple.domain.model.Algorithm
import com.ivaniuk.algolearnsimple.domain.model.AlgorithmCategory
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import com.ivaniuk.algolearnsimple.domain.usecase.GetAlgorithmsUseCase
import com.ivaniuk.algolearnsimple.domain.usecase.ToggleFavoriteUseCase
import com.ivaniuk.algolearnsimple.utils.MainDispatcherRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getAlgorithmsUseCase: GetAlgorithmsUseCase

    @Mock
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    @Mock
    private lateinit var repository: AlgorithmRepository

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        `when`(getAlgorithmsUseCase()).thenReturn(flowOf(emptyList()))
        viewModel = HomeViewModel(getAlgorithmsUseCase, toggleFavoriteUseCase, repository)
    }

    @Test
    fun testToggleFavorite() = runTest {
        viewModel.toggleFavorite(1)

        verify(toggleFavoriteUseCase).invoke(1)
    }

    @Test
    fun testLoadAlgorithmById() = runTest {
        val expectedAlgorithm = Algorithm(1, "Bubble Sort", "", AlgorithmCategory.SORTING, "O(n²)", "", emptyList(), false)
        `when`(repository.getAlgorithmById(1)).thenReturn(expectedAlgorithm)

        viewModel.loadAlgorithmById(1)
        advanceUntilIdle()

        assertEquals(expectedAlgorithm, viewModel.currentAlgorithm.value)
    }

    @Test
    fun testLoadAlgorithmByIdReturnsNull() = runTest {
        `when`(repository.getAlgorithmById(999)).thenReturn(null)

        viewModel.loadAlgorithmById(999)
        advanceUntilIdle()

        assertNull(viewModel.currentAlgorithm.value)
    }
}
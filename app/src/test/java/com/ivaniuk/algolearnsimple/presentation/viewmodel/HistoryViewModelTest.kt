package com.ivaniuk.algolearnsimple.presentation.viewmodel

import com.ivaniuk.algolearnsimple.domain.usecase.ClearHistoryUseCase
import com.ivaniuk.algolearnsimple.domain.usecase.GetRecentHistoryUseCase
import com.ivaniuk.algolearnsimple.utils.MainDispatcherRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class HistoryViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getRecentHistoryUseCase: GetRecentHistoryUseCase

    @Mock
    private lateinit var clearHistoryUseCase: ClearHistoryUseCase

    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        `when`(getRecentHistoryUseCase()).thenReturn(flowOf(emptyList()))
        viewModel = HistoryViewModel(getRecentHistoryUseCase, clearHistoryUseCase)
    }

    @Test
    fun testClearsHistory() = runTest {
        viewModel.clearHistory()

        advanceUntilIdle()
        verify(clearHistoryUseCase).invoke()
    }
}
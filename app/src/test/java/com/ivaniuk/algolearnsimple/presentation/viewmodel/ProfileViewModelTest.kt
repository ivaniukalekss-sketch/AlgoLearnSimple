package com.ivaniuk.algolearnsimple.presentation.viewmodel

import com.ivaniuk.algolearnsimple.domain.model.Algorithm
import com.ivaniuk.algolearnsimple.domain.model.AlgorithmCategory
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
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

class ProfileViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: AlgorithmRepository

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = ProfileViewModel(repository)
    }

    @Test
    fun testLoadStatsUpdatesViewedCount() = runTest {
        val expectedCount = 5
        `when`(repository.getViewedCount()).thenReturn(expectedCount)
        `when`(repository.getFavoriteAlgorithms()).thenReturn(flowOf(emptyList()))

        viewModel.loadStats()

        // Задержка для асинхронной операции
        advanceUntilIdle()
        assertEquals(expectedCount, viewModel.viewedCount.value)
    }

    @Test
    fun testLoadStatsUpdatesFavoriteCount() = runTest {
        val favorites = listOf(
            Algorithm(1, "Bubble Sort", "", AlgorithmCategory.SORTING, "O(n²)", "", emptyList(), true)
        )
        `when`(repository.getViewedCount()).thenReturn(0)
        `when`(repository.getFavoriteAlgorithms()).thenReturn(flowOf(favorites))

        viewModel.loadStats()

        advanceUntilIdle()
        assertEquals(1, viewModel.favoriteCount.value)
    }

    @Test
    fun testRefreshStatsRefreshesData() = runTest {
        `when`(repository.getViewedCount()).thenReturn(3)
        `when`(repository.getFavoriteAlgorithms()).thenReturn(flowOf(emptyList()))

        viewModel.refreshStats()

        advanceUntilIdle()
        assertEquals(3, viewModel.viewedCount.value)
    }

    @Test
    fun testAchievementsUnlockOnFirstView() = runTest {
        `when`(repository.getViewedCount()).thenReturn(1)
        `when`(repository.getFavoriteAlgorithms()).thenReturn(flowOf(emptyList()))

        viewModel.loadStats()
        advanceUntilIdle()

        val achievements = viewModel.achievements.value
        val firstViewAchievement = achievements.find { it.id == "first_view" }
        assertNotNull(firstViewAchievement)
        assertTrue(firstViewAchievement?.isUnlocked == true)
    }
}
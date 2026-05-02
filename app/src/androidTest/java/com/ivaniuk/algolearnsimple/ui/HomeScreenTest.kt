package com.ivaniuk.algolearnsimple.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ivaniuk.algolearnsimple.presentation.screens.HomeScreen
import com.ivaniuk.algolearnsimple.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHomeScreenDisplays() {
        val testAlgorithms = MutableStateFlow(
            listOf(
                com.ivaniuk.algolearnsimple.domain.model.Algorithm(
                    1, "Bubble Sort", "", com.ivaniuk.algolearnsimple.domain.model.AlgorithmCategory.SORTING,
                    "O(n²)", "", emptyList(), false
                )
            )
        )

        val mockViewModel = object : HomeViewModel(
            getAlgorithmsUseCase = com.ivaniuk.algolearnsimple.domain.usecase.GetAlgorithmsUseCase(
                mock()
            ),
            toggleFavoriteUseCase = com.ivaniuk.algolearnsimple.domain.usecase.ToggleFavoriteUseCase(
                mock()
            ),
            repository = mock()
        ) {
            override val algorithms = testAlgorithms.asStateFlow()
        }

        composeTestRule.setContent {
            HomeScreen(
                viewModel = mockViewModel,
                onAlgorithmClick = {},
                onToggleFavorite = {},
                onFavoritesClick = {},
                onHistoryClick = {},
                onVisualizeClick = {},
                onProfileClick = {}
            )
        }

        composeTestRule.onRoot().printToLog("HomeScreen")
        composeTestRule.onNodeWithText("Bubble Sort").assertExists()
    }

    private fun mock() = org.mockito.Mockito.mock(com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository::class.java)
}
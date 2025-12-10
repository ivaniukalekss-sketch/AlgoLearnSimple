package com.ivaniuk.algolearnsimple.presentation.navigation

import com.ivaniuk.algolearnsimple.domain.visualizer.QuickSortVisualizer
import com.ivaniuk.algolearnsimple.domain.visualizer.DFSVisualizer
import com.ivaniuk.algolearnsimple.domain.visualizer.BubbleSortVisualizer
import com.ivaniuk.algolearnsimple.presentation.screens.VisualizationScreen
import com.ivaniuk.algolearnsimple.presentation.viewmodel.VisualizationViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ivaniuk.algolearnsimple.AppContainer
import com.ivaniuk.algolearnsimple.presentation.screens.AlgorithmDetailScreen
import com.ivaniuk.algolearnsimple.presentation.screens.FavoritesScreen
import com.ivaniuk.algolearnsimple.presentation.screens.HomeScreen
import com.ivaniuk.algolearnsimple.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import com.ivaniuk.algolearnsimple.domain.visualizer.BinarySearchVisualizer

@Composable
fun AppNavigation(
    appContainer: AppContainer
) {
    val navController = rememberNavController()
    val viewModel = HomeViewModel(appContainer.algorithmRepository)
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onAlgorithmClick = { algorithmId ->
                    navController.navigate("algorithm/$algorithmId")
                },
                onToggleFavorite = { algorithmId ->
                    coroutineScope.launch {
                        viewModel.toggleFavorite(algorithmId)
                    }
                },
                onFavoritesClick = {
                    navController.navigate("favorites")
                },
                onVisualizeClick = { algorithmId ->
                    navController.navigate("visualization/$algorithmId")
                }
            )
        }

        composable("algorithm/{algorithmId}") { backStackEntry ->
            val algorithmId = backStackEntry.arguments?.getString("algorithmId")?.toIntOrNull()
            val algorithm by viewModel.algorithms.collectAsState()
            val currentAlgorithm = algorithm.find { it.id == algorithmId }

            AlgorithmDetailScreen(
                algorithm = currentAlgorithm,
                onBack = { navController.navigateUp() },
                onToggleFavorite = {
                    algorithmId?.let {
                        coroutineScope.launch {
                            viewModel.toggleFavorite(it)
                        }
                    }
                },
                onVisualizeClick = {
                    algorithmId?.let {
                        navController.navigate("visualization/$it")
                    }
                }
            )
        }

        composable("favorites") {
            FavoritesScreen(
                viewModel = viewModel,
                onAlgorithmClick = { algorithmId ->
                    navController.navigate("algorithm/$algorithmId")
                },
                onToggleFavorite = { algorithmId ->
                    coroutineScope.launch {
                        viewModel.toggleFavorite(algorithmId)
                    }
                },

                onBack = { navController.navigateUp() }
            )
        }
        composable("visualization/{algorithmId}") { backStackEntry ->
            val algorithmId = backStackEntry.arguments?.getString("algorithmId")?.toIntOrNull()
            val visualizer = when (algorithmId) {
                1 -> BubbleSortVisualizer()
                2 -> BinarySearchVisualizer()
                3 -> QuickSortVisualizer()
                4 -> DFSVisualizer()
                else -> BubbleSortVisualizer()
            }

            val viewModel = VisualizationViewModel(visualizer)

            VisualizationScreen(
                viewModel = viewModel,
                onBack = { navController.navigateUp() }
            )
        }
    }
}
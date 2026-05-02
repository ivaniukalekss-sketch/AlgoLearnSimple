package com.ivaniuk.algolearnsimple.presentation.navigation

import com.ivaniuk.algolearnsimple.domain.visualizer.QuickSortVisualizer
import com.ivaniuk.algolearnsimple.domain.visualizer.DFSVisualizer
import com.ivaniuk.algolearnsimple.domain.visualizer.BubbleSortVisualizer
import com.ivaniuk.algolearnsimple.presentation.screens.VisualizationScreen
import com.ivaniuk.algolearnsimple.presentation.viewmodel.VisualizationViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ivaniuk.algolearnsimple.domain.visualizer.BFSVisualizer
import com.ivaniuk.algolearnsimple.presentation.screens.AlgorithmDetailScreen
import com.ivaniuk.algolearnsimple.presentation.screens.FavoritesScreen
import com.ivaniuk.algolearnsimple.presentation.screens.HomeScreen
import com.ivaniuk.algolearnsimple.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import com.ivaniuk.algolearnsimple.domain.visualizer.BinarySearchVisualizer
import androidx.hilt.navigation.compose.hiltViewModel
import com.ivaniuk.algolearnsimple.presentation.screens.HistoryScreen
import com.ivaniuk.algolearnsimple.domain.visualizer.MergeSortVisualizer
import com.ivaniuk.algolearnsimple.domain.visualizer.DijkstraVisualizer
import com.ivaniuk.algolearnsimple.presentation.screens.ProfileScreen
import com.ivaniuk.algolearnsimple.domain.visualizer.SelectionSortVisualizer
import com.ivaniuk.algolearnsimple.domain.visualizer.InsertionSortVisualizer
import com.ivaniuk.algolearnsimple.domain.visualizer.LinearSearchVisualizer
import com.ivaniuk.algolearnsimple.presentation.screens.TutorialScreen
import com.ivaniuk.algolearnsimple.presentation.viewmodel.TutorialViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember

@Composable
fun AppNavigation() {
    val tutorialViewModel: TutorialViewModel = hiltViewModel()
    var showTutorial by remember {
        mutableStateOf(!tutorialViewModel.isTutorialSeen())
    }

    if (showTutorial) {
        TutorialScreen(
            onComplete = {
                tutorialViewModel.setTutorialSeen()
                showTutorial = false
            }
        )
    } else {
        val viewModel: HomeViewModel = hiltViewModel()
        val navController = rememberNavController()
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
                    },
                    onHistoryClick = { navController.navigate("history") },
                    onProfileClick = { navController.navigate("profile") }
                )
            }

            composable("algorithm/{algorithmId}") { backStackEntry ->
                val algorithmId = backStackEntry.arguments?.getString("algorithmId")?.toIntOrNull()

                LaunchedEffect(algorithmId) {
                    algorithmId?.let {
                        viewModel.loadAlgorithmById(it)
                    }
                }

                val currentAlgorithm by viewModel.currentAlgorithm.collectAsState()

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
                    5 -> BFSVisualizer()
                    6 -> MergeSortVisualizer()
                    7 -> DijkstraVisualizer()
                    8 -> SelectionSortVisualizer()
                    9 -> InsertionSortVisualizer()
                    10 -> LinearSearchVisualizer()
                    else -> BubbleSortVisualizer()
                }

                val viewModel = VisualizationViewModel(visualizer)

                VisualizationScreen(
                    viewModel = viewModel,
                    onBack = { navController.navigateUp() }
                )
            }

            composable("history") {
                HistoryScreen(
                    onBack = { navController.navigateUp() },
                    onAlgorithmClick = { algorithmId ->
                        navController.navigate("algorithm/$algorithmId")
                    }
                )
            }
            composable("profile") {
                ProfileScreen(
                    onBack = { navController.navigateUp() }
                ) { showTutorial = true }
            }
        }
    }
}
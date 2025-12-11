package com.ivaniuk.algolearnsimple.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ivaniuk.algolearnsimple.presentation.components.AlgorithmCard
import com.ivaniuk.algolearnsimple.presentation.viewmodel.HomeViewModel
import androidx.compose.material.icons.filled.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAlgorithmClick: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onFavoritesClick: () -> Unit,
    onVisualizeClick: (Int) -> Unit,
    onStatisticsClick: () -> Unit
) {
    val algorithms by viewModel.algorithms.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val filteredAlgorithms = if (searchText.isBlank()) {
        algorithms
    } else {
        algorithms.filter { algorithm ->
            algorithm.title.contains(searchText, ignoreCase = true) ||
                    algorithm.description.contains(searchText, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "AlgoLearn",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        // Кнопка статистики
                        IconButton(onClick = onStatisticsClick) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Статистика"
                            )
                        }
                        // Кнопка избранного
                        IconButton(onClick = onFavoritesClick) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Избранное"
                            )
                        }
                    }
                )

                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    query = searchText,
                    onQueryChange = { searchText = it },
                    onSearch = { active = false },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text("Поиск алгоритмов...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Поиск"
                        )
                    }
                ) {}
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                algorithms.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Загрузка алгоритмов...",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }

                filteredAlgorithms.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Алгоритмы не найдены",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredAlgorithms) { algorithm ->
                            AlgorithmCard(
                                algorithm = algorithm,
                                onCardClick = { onAlgorithmClick(algorithm.id) },
                                onFavoriteClick = { onToggleFavorite(algorithm.id) },
                                onVisualizeClick = { onVisualizeClick(algorithm.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.ivaniuk.algolearnsimple.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ivaniuk.algolearnsimple.domain.model.Algorithm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlgorithmDetailScreen(
    algorithm: Algorithm?,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onVisualizeClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(algorithm?.title ?: "Детали алгоритма") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    if (algorithm != null) {
                        IconButton(
                            onClick = onVisualizeClick,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircleOutline,
                                contentDescription = "Запустить визуализацию",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    if (algorithm != null) {
                        IconButton(onClick = onToggleFavorite) {
                            Icon(
                                imageVector = if (algorithm.isFavorite) {
                                    Icons.Default.Favorite
                                } else {
                                    Icons.Outlined.FavoriteBorder
                                },
                                contentDescription = "Избранное",
                                tint = if (algorithm.isFavorite) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        if (algorithm == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Алгоритм не найден",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBack) {
                    Text("Вернуться назад")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = algorithm.title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Категория: ${algorithm.category.name}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Сложность: ${algorithm.complexity}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Text(
                    text = "Описание",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = algorithm.description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify
                )

                if (algorithm.steps.isNotEmpty()) {
                    Text(
                        text = "Шаги выполнения",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    algorithm.steps.forEachIndexed { index, step ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Шаг ${index + 1}",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = step,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "Пример кода на Kotlin",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = algorithm.codeExample,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Что означает ${algorithm.complexity}?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val explanation = when (algorithm.complexity) {
                            "O(n²)" -> """
                Квадратичная сложность означает, что при увеличении размера данных в 10 раз,
                время работы увеличится примерно в 100 раз.
                
                 Пример: 
                • n = 10 → ~100 операций
                • n = 100 → ~10,000 операций
                • n = 1000 → ~1,000,000 операций
                
                 Используйте только для маленьких данных!
            """.trimIndent()

                            "O(log n)" -> """
                Логарифмическая сложность - очень эффективна!
                При увеличении данных в 1000 раз, время увеличится всего в 10 раз.
                
                 Пример:
                • n = 10 → ~3 операции
                • n = 100 → ~7 операций
                • n = 1,000,000 → ~20 операций
                
                 Идеально для поиска в больших данных!
            """.trimIndent()

                            "O(n log n)" -> """
                Линейно-логарифмическая сложность - оптимальна для сортировки.
                Хорошо масштабируется на большие объёмы данных.
                
                 Пример:
                • n = 10 → ~30 операций
                • n = 100 → ~700 операций
                • n = 1000 → ~10,000 операций
                
                 Стандарт для алгоритмов сортировки!
            """.trimIndent()

                            "O(V + E)" -> """
                Линейная сложность для графов.
                Зависит от количества вершин (V) и рёбер (E).
                
                Пример:
                • 10 вершин, 15 рёбер → ~25 операций
                • 100 вершин, 200 рёбер → ~300 операций
                • 1000 вершин, 3000 рёбер → ~4000 операций
                
                Время растёт пропорционально размеру графа.
            """.trimIndent()

                            else -> "Стандартная сложность алгоритма."
                        }

                        Text(
                            text = explanation,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
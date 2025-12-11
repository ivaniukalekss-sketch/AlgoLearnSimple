package com.ivaniuk.algolearnsimple.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ivaniuk.algolearnsimple.presentation.viewmodel.StatisticsViewModel
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    onBack: () -> Unit,
    onAlgorithmClick: (Int) -> Unit
) {
    val userStats by viewModel.userStatistics.collectAsState()
    val recentSessions by viewModel.recentSessions.collectAsState()
    val weeklyStats by viewModel.weeklyStats.collectAsState()
    val learningStreak by viewModel.learningStreak.collectAsState()
    val mostStudied by viewModel.mostStudiedAlgorithm.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "📊 Статистика изучения",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // Показать диалог сброса статистики
                        }
                    ) {
                        Icon(Icons.Default.RestartAlt, contentDescription = "Сбросить")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Секция: Общая статистика
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "📈 Общая статистика",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        StatGrid(
                            stats = listOf(
                                StatItem("Серия обучения", "${learningStreak} дней", Icons.Default.LocalFireDepartment),
                                StatItem("Всего изучено", "${userStats?.algorithmsLearned ?: 0}/5", Icons.Default.CheckCircle),
                                StatItem("Сессий изучения", "${userStats?.totalSessions ?: 0}", Icons.Default.PlayArrow),
                                StatItem("Общее время", formatDuration(userStats?.totalStudyTime ?: 0), Icons.Default.Timer)
                            )
                        )

                        if (mostStudied != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "⭐ Любимый алгоритм: ${mostStudied?.second ?: "Не определён"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Секция: Недавние сессии
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "🕐 Недавние сессии",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        if (recentSessions.isEmpty()) {
                            Text(
                                text = "Нет данных о сессиях",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            recentSessions.forEach { session ->
                                SessionItem(
                                    session = session,
                                    onClick = { onAlgorithmClick(session.algorithmId) }
                                )
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                }
            }

            // Секция: Прогресс по алгоритмам
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "🎯 Прогресс по алгоритмам",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Пример прогресса для 5 алгоритмов
                        AlgorithmProgress(
                            algorithmName = "Bubble Sort",
                            progress = 85f,
                            timeSpent = "2ч 30м"
                        )
                        AlgorithmProgress(
                            algorithmName = "Binary Search",
                            progress = 95f,
                            timeSpent = "1ч 45м"
                        )
                        AlgorithmProgress(
                            algorithmName = "Quick Sort",
                            progress = 70f,
                            timeSpent = "3ч 15м"
                        )
                        AlgorithmProgress(
                            algorithmName = "DFS",
                            progress = 60f,
                            timeSpent = "1ч 20м"
                        )
                        AlgorithmProgress(
                            algorithmName = "Dijkstra",
                            progress = 50f,
                            timeSpent = "2ч 10м"
                        )
                    }
                }
            }

            // Секция: Недельная активность
            item {
                if (weeklyStats.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "📅 Активность за неделю",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            WeeklyActivityChart(stats = weeklyStats)
                        }
                    }
                }
            }

            // Секция: Достижения
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "🏆 Достижения",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        AchievementGrid()
                    }
                }
            }
        }
    }
}

@Composable
private fun StatGrid(
    stats: List<StatItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        stats.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.value,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Заполнение пустых ячеек
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SessionItem(
    session: com.ivaniuk.algolearnsimple.domain.model.StudyStatistic,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = session.algorithmName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = session.dateTime.format(
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = session.getFormattedDuration(),
                    style = MaterialTheme.typography.bodyMedium
                )
                LinearProgressIndicator(
                    progress = session.completionPercentage / 100f,
                    modifier = Modifier
                        .width(80.dp)
                        .padding(top = 4.dp),
                    color = when {
                        session.completionPercentage >= 80 -> Color(0xFF4CAF50)
                        session.completionPercentage >= 50 -> Color(0xFF2196F3)
                        else -> Color(0xFFFF9800)
                    }
                )
            }
        }
    }
}

@Composable
private fun AlgorithmProgress(
    algorithmName: String,
    progress: Float,
    timeSpent: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = algorithmName,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${progress.roundToInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = progress / 100f,
            modifier = Modifier.fillMaxWidth(),
            color = when {
                progress >= 80 -> Color(0xFF4CAF50)
                progress >= 50 -> Color(0xFF2196F3)
                else -> Color(0xFFFF9800)
            },
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Время изучения: $timeSpent",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun WeeklyActivityChart(
    stats: Map<String, com.ivaniuk.algolearnsimple.domain.model.DailyStatistic>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Простая гистограмма активности
            val maxTime = stats.values.maxOfOrNull { it.studyTime } ?: 1L

            stats.entries.sortedBy { it.key }.forEach { (date, stat) ->
                val dayName = date.substring(8, 10) // День месяца
                val height = (stat.studyTime.toFloat() / maxTime.toFloat() * 60).dp

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(height)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dayName,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = formatShortDuration(stat.studyTime),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun AchievementGrid() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AchievementBadge(
            icon = Icons.Default.EmojiEvents,
            title = "Новичок",
            description = "Завершить первую сессию",
            achieved = true
        )
        AchievementBadge(
            icon = Icons.Default.Star,
            title = "Специалист",
            description = "Изучить 3 алгоритма",
            achieved = true
        )
        AchievementBadge(
            icon = Icons.Default.AutoAwesome,
            title = "Мастер",
            description = "5 дней подряд",
            achieved = false
        )
    }
}

@Composable
private fun AchievementBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    achieved: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(48.dp),
            tint = if (achieved) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = description,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

// Вспомогательные функции
private fun formatDuration(millis: Long): String {
    val hours = millis / (1000 * 60 * 60)
    val minutes = (millis % (1000 * 60 * 60)) / (1000 * 60)

    return when {
        hours > 0 -> "${hours}ч ${minutes}м"
        minutes > 0 -> "${minutes}м"
        else -> "< 1м"
    }
}

private fun formatShortDuration(millis: Long): String {
    val minutes = millis / (1000 * 60)
    return "${minutes}м"
}

data class StatItem(
    val label: String,
    val value: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
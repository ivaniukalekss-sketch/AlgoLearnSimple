package com.ivaniuk.algolearnsimple.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ArrayVisualizer(
    array: List<Int>,
    highlightedIndices: Set<Int> = emptySet(),
    comparingIndices: Set<Int> = emptySet(),
    swappedIndices: Set<Int> = emptySet(),
    sortedIndices: Set<Int> = emptySet(),
    currentIndex: Int? = null,
    modifier: Modifier = Modifier
) {
    val maxValue = if (array.isNotEmpty()) array.maxOrNull() ?: 1 else 1
    val maxBarHeight = 220.dp

    // Определяем размер текста на основе количества элементов
    val valueTextSize = when {
        array.size <= 8 -> 14.sp
        array.size <= 15 -> 12.sp
        array.size <= 25 -> 10.sp
        else -> 8.sp
    }

    val indexTextSize = when {
        array.size <= 8 -> 11.sp
        array.size <= 15 -> 10.sp
        array.size <= 25 -> 9.sp
        else -> 8.sp
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Массив (${array.size} элементов):",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(bottom = 12.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                array.forEachIndexed { index, value ->
                    // Вычисляем высоту
                    val normalizedHeight = if (maxValue > 0) {
                        (value.toFloat() / maxValue.toFloat()) * maxBarHeight.value
                    } else {
                        maxBarHeight.value
                    }
                    val targetHeight = normalizedHeight.dp

                    // Цвет
                    val targetColor = when {
                        index in comparingIndices -> Color(0xFFFFA726)
                        index in swappedIndices -> Color(0xFFEF5350)
                        index in sortedIndices -> Color(0xFF66BB6A)
                        index in highlightedIndices -> Color(0xFF42A5F5)
                        index == currentIndex -> Color(0xFF673AB7)
                        else -> MaterialTheme.colorScheme.primary
                    }

                    // Анимация высоты
                    val animatedHeight by animateDpAsState(
                        targetValue = targetHeight,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                        label = "height_$index"
                    )

                    // Анимация цвета
                    val animatedColor by animateColorAsState(
                        targetValue = targetColor,
                        animationSpec = tween(durationMillis = 200),
                        label = "color_$index"
                    )

                    // Цвет текста
                    val textColor = if (animatedColor == MaterialTheme.colorScheme.primary) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        Color.White
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Столбец
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(animatedHeight)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(animatedColor)
                                    .padding(vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = value.toString(),
                                    color = textColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = valueTextSize,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    modifier = Modifier.padding(horizontal = 2.dp)
                                )
                            }

                            // Индекс
                            Text(
                                text = "[$index]",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = indexTextSize
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Легенда
        val legendItems = listOf(
            LegendItem(Color(0xFF66BB6A), "Отсортировано"),
            LegendItem(Color(0xFFFFA726), "Сравнение"),
            LegendItem(Color(0xFFEF5350), "Обмен"),
            LegendItem(Color(0xFF42A5F5), "Выделено"),
            LegendItem(Color(0xFF673AB7), "Текущий")
        )

        Legend(
            legendItems = legendItems,
            modifier = Modifier.fillMaxWidth()
        )

        if (array.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Значения: min=${array.minOrNull()}, max=$maxValue",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
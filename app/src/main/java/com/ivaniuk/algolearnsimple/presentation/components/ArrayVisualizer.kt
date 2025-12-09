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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    // Вычисляем максимальное значение для нормализации высоты
    val maxValue = if (array.isNotEmpty()) array.maxOrNull() ?: 1 else 1
    // Ограничиваем максимальную высоту столбца
    val maxBarHeight = 150.dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок с количеством элементов
        Text(
            text = "Массив (${array.size} элементов):",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            array.forEachIndexed { index, value ->
                // Вычисляем высоту правильно
                val normalizedHeight = if (maxValue > 0) {
                    (value.toFloat() / maxValue.toFloat()) * maxBarHeight.value
                } else {
                    maxBarHeight.value
                }
                val targetHeight = normalizedHeight.dp

                // Цвет в зависимости от состояния
                val targetColor = when {
                    index in comparingIndices -> Color(0xFFFFA726) // Оранжевый
                    index in swappedIndices -> Color(0xFFEF5350)   // Красный
                    index in sortedIndices -> Color(0xFF66BB6A)    // Зелёный
                    index in highlightedIndices -> Color(0xFF42A5F5) // Синий
                    index == currentIndex -> Color(0xFFAB47BC)     // Фиолетовый
                    else -> MaterialTheme.colorScheme.primary      // Основной
                }

                // Анимация высоты
                val animatedHeight by animateDpAsState(
                    targetValue = targetHeight,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    ),
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

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Анимированная коробка (СТОЛБЕЦ)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(animatedHeight)
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(animatedColor)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = value.toString(),
                            color = textColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }

                    // Индекс под столбцом
                    Text(
                        text = "[$index]",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Легенда цветов (только если есть что показывать)
        val hasAnyHighlights = highlightedIndices.isNotEmpty() ||
                comparingIndices.isNotEmpty() ||
                swappedIndices.isNotEmpty() ||
                sortedIndices.isNotEmpty()

        if (hasAnyHighlights) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Обозначения:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (sortedIndices.isNotEmpty()) {
                    LegendItem(color = Color(0xFF66BB6A), text = "Отсортировано")
                }
                if (comparingIndices.isNotEmpty()) {
                    LegendItem(color = Color(0xFFFFA726), text = "Сравнение")
                }
                if (swappedIndices.isNotEmpty()) {
                    LegendItem(color = Color(0xFFEF5350), text = "Обмен")
                }
                if (highlightedIndices.isNotEmpty()) {
                    LegendItem(color = Color(0xFF42A5F5), text = "Выделено")
                }
                if (currentIndex != null) {
                    LegendItem(color = Color(0xFFAB47BC), text = "Текущий")
                }
            }
        }

        // Информация о диапазоне значений
        if (array.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Значения: min=${array.minOrNull()}, max=$maxValue",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
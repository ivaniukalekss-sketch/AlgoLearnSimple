package com.ivaniuk.algolearnsimple.presentation.components

// ★★★ ПРАВИЛЬНЫЕ ИМПОРТЫ ДЛЯ АНИМАЦИЙ ★★★
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Массив:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            array.forEachIndexed { index, value ->
                // Цвет в зависимости от состояния
                val targetColor = when {
                    index in comparingIndices -> Color(0xFFFFA726) // Оранжевый
                    index in swappedIndices -> Color(0xFFEF5350)   // Красный
                    index in sortedIndices -> Color(0xFF66BB6A)    // Зелёный
                    index in highlightedIndices -> Color(0xFF42A5F5) // Синий
                    index == currentIndex -> Color(0xFFAB47BC)     // Фиолетовый
                    else -> MaterialTheme.colorScheme.primary      // Основной
                }

                // ★★★ АНИМАЦИЯ ВЫСОТЫ ★★★
                val animatedHeight by animateDpAsState(
                    targetValue = (value * 20).dp,
                    animationSpec = tween(
                        durationMillis = 500, // Полсекунды - хорошо видно
                        easing = FastOutSlowInEasing
                    ),
                    label = "height_$index"
                )

                // ★★★ АНИМАЦИЯ ЦВЕТА ★★★
                val animatedColor by animateColorAsState(
                    targetValue = targetColor,
                    animationSpec = tween(durationMillis = 300),
                    label = "color_$index"
                )

                // Цвет текста (белый на цветном фоне, контрастный на основном)
                val textColor = if (animatedColor == MaterialTheme.colorScheme.primary) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    Color.White
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Анимированная коробка
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(animatedHeight)
                            .clip(RoundedCornerShape(8.dp))
                            .background(animatedColor)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = value.toString(),
                            color = textColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Индекс под коробкой
                    Text(
                        text = "[$index]",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Легенда цветов
        if (highlightedIndices.isNotEmpty() || comparingIndices.isNotEmpty() ||
            swappedIndices.isNotEmpty() || sortedIndices.isNotEmpty()) {

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                if (sortedIndices.isNotEmpty()) {
                    LegendItem(color = Color(0xFF66BB6A), text = "✓ Отсортировано")
                }
                if (comparingIndices.isNotEmpty()) {
                    LegendItem(color = Color(0xFFFFA726), text = "↔ Сравнение")
                }
                if (swappedIndices.isNotEmpty()) {
                    LegendItem(color = Color(0xFFEF5350), text = "⇄ Обмен")
                }
                if (highlightedIndices.isNotEmpty()) {
                    LegendItem(color = Color(0xFF42A5F5), text = "● Выделено")
                }
            }
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
                .size(12.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
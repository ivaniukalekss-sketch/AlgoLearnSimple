package com.ivaniuk.algolearnsimple.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun WeightedGraphLegend(modifier: Modifier = Modifier) {
    // Создаем список элементов легенды для Дейкстры
    val legendItems = listOf(
        LegendItem(Color(0xFF2196F3), "Старт"),
        LegendItem(Color(0xFFFF9800), "Цель"),
        LegendItem(Color(0xFF66BB6A), "Посещён"),
        LegendItem(Color.Blue, "Ребро пути", showCircle = false, showLine = true)
    )

    Legend(
        legendItems = legendItems,
        modifier = modifier
    )
}

@Composable
private fun SingleLegendItem(item: LegendItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.width(80.dp)
    ) {
        if (item.showCircle) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(item.color)
            )
        } else if (item.showLine) {
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(3.dp)
                    .background(item.color)
            )
        }
        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
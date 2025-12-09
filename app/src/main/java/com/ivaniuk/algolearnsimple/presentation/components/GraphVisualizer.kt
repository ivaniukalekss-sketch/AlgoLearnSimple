package com.ivaniuk.algolearnsimple.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

data class GraphNode(
    val id: Int,
    val position: Offset,
    val radius: Float = 40f
)

@Composable
fun GraphVisualizer(
    graph: Map<Int, List<Int>>,
    highlightedNodes: Set<Int> = emptySet(),
    currentNodes: Set<Int> = emptySet(),
    visitedNodes: Set<Int> = emptySet(),
    modifier: Modifier = Modifier,
    size: DpSize = DpSize(350.dp, 300.dp)
) {
    val textMeasurer = rememberTextMeasurer()
    val nodes = remember(graph) { calculateNodePositions(graph) }

    // Получаем цвета из MaterialTheme ДО Canvas
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = modifier
            .size(size)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Рисуем рёбра
            graph.forEach { (nodeId, neighbors) ->
                val node = nodes.find { it.id == nodeId }
                node?.let { source ->
                    neighbors.forEach { neighborId ->
                        val target = nodes.find { it.id == neighborId }
                        target?.let {
                            drawLine(
                                color = Color.Gray.copy(alpha = 0.5f),
                                start = source.position,
                                end = target.position,
                                strokeWidth = 2f
                            )
                        }
                    }
                }
            }

            // Рисуем узлы
            nodes.forEach { node ->
                // Цвет узла
                val color = when {
                    node.id in currentNodes -> Color(0xFFFFA726) // Оранжевый - текущий
                    node.id in visitedNodes -> Color(0xFF66BB6A) // Зелёный - посещённый
                    node.id in highlightedNodes -> Color(0xFF42A5F5) // Синий - выделенный
                    else -> primaryColor // Основной
                }

                // Отрисовка узла
                drawCircle(
                    color = color,
                    center = node.position,
                    radius = node.radius,
                    style = Stroke(width = 3f)
                )

                drawCircle(
                    color = color.copy(alpha = 0.2f),
                    center = node.position,
                    radius = node.radius
                )

                // Отрисовка ID узла
                val text = node.id.toString()
                val textLayoutResult = textMeasurer.measure(text)

                // Цвет текста
                val textColor = if (node.id in visitedNodes || node.id in currentNodes) {
                    Color.White
                } else {
                    onPrimaryColor
                }

                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(
                        node.position.x - textLayoutResult.size.width / 2,
                        node.position.y - textLayoutResult.size.height / 2
                    ),
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun GraphLegend(
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(color = Color(0xFFFFA726), text = "Текущий узел")
        LegendItem(color = Color(0xFF66BB6A), text = "Посещённый")
        LegendItem(color = Color(0xFF42A5F5), text = "Выделенный")
        LegendItem(color = primaryColor, text = "Обычный")
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
                .background(color, MaterialTheme.shapes.small)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun calculateNodePositions(graph: Map<Int, List<Int>>): List<GraphNode> {
    val nodes = graph.keys.toList()
    val center = Offset(200f, 150f)
    val radius = 100f

    return nodes.mapIndexed { index, nodeId ->
        val angle = 2 * Math.PI * index / nodes.size
        val x = center.x + radius * cos(angle).toFloat()
        val y = center.y + radius * sin(angle).toFloat()
        GraphNode(nodeId, Offset(x, y))
    }
}
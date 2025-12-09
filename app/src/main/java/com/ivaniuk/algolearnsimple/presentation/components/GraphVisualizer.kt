package com.ivaniuk.algolearnsimple.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.min

data class GraphNode(
    val id: Int,
    val position: Offset,
    val radius: Float = 30f
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
    // Конвертируем Dp в пиксели
    val density = LocalDensity.current
    val widthPx = with(density) { size.width.toPx() }
    val heightPx = with(density) { size.height.toPx() }

    // Получаем цвета из MaterialTheme
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
            // Рассчитываем позиции узлов
            val nodes = calculateNodePositions(
                graph = graph,
                canvasWidth = widthPx,
                canvasHeight = heightPx
            )

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

                // ID узла (просто цифра) - используем Android Paint
                val paint = android.graphics.Paint().apply {
                    this.color = if (node.id in visitedNodes || node.id in currentNodes) {
                        android.graphics.Color.WHITE
                    } else {
                        android.graphics.Color.BLACK
                    }
                    textSize = 24f
                    textAlign = android.graphics.Paint.Align.CENTER
                }

                drawContext.canvas.nativeCanvas.drawText(
                    node.id.toString(),
                    node.position.x,
                    node.position.y + 10f, // Центрируем текст
                    paint
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

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Обозначения:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(color = Color(0xFFFFA726), text = "Текущий")
            LegendItem(color = Color(0xFF66BB6A), text = "Посещённый")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(color = Color(0xFF42A5F5), text = "Выделенный")
            LegendItem(color = primaryColor, text = "Обычный")
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
                .background(color, MaterialTheme.shapes.small)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun calculateNodePositions(
    graph: Map<Int, List<Int>>,
    canvasWidth: Float,
    canvasHeight: Float
): List<GraphNode> {
    val nodes = graph.keys.toList()
    if (nodes.isEmpty()) return emptyList()

    // Центр Canvas
    val centerX = canvasWidth / 2
    val centerY = canvasHeight / 2

    // Радиус круга - 35% от меньшей стороны
    val minSide = min(canvasWidth, canvasHeight)
    val radius = minSide * 0.35f

    // Радиус узлов адаптивный
    val nodeRadius = min(25f, radius * 0.15f)

    return nodes.mapIndexed { index, nodeId ->
        val angle = 2 * PI * index / nodes.size
        val x = centerX + radius * cos(angle).toFloat()
        val y = centerY + radius * sin(angle).toFloat()
        GraphNode(nodeId, Offset(x, y), radius = nodeRadius)
    }
}
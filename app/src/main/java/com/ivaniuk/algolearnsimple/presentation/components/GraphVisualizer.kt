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
    size: DpSize = DpSize(350.dp, 300.dp),
    showLegend: Boolean = true
) {
    val density = LocalDensity.current
    val widthPx = with(density) { size.width.toPx() }
    val heightPx = with(density) { size.height.toPx() }

    val primaryColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Заголовок с информацией о графе
        Text(
            text = "Граф (${graph.keys.size} вершин, ${graph.values.sumOf { it.size } / 2} рёбер):",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Box(
            modifier = Modifier
                .size(size)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Функция внутри лямбды Canvas
                fun calculateNodePositions(
                    graph: Map<Int, List<Int>>,
                    canvasWidth: Float,
                    canvasHeight: Float
                ): List<GraphNode> {
                    val nodes = graph.keys.toList()
                    if (nodes.isEmpty()) return emptyList()

                    val centerX = canvasWidth / 2
                    val centerY = canvasHeight / 2

                    val minSide = min(canvasWidth, canvasHeight)
                    val radius = minSide * 0.40f

                    val nodeRadius = min(30f, radius * 0.18f)

                    return nodes.mapIndexed { index, nodeId ->
                        val angle = 2 * PI * index / nodes.size
                        val x = centerX + radius * cos(angle).toFloat()
                        val y = centerY + radius * sin(angle).toFloat()
                        GraphNode(nodeId, Offset(x, y), radius = nodeRadius)
                    }
                }

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

                // Рисуем вершины
                nodes.forEach { node ->
                    val color = when (node.id) {
                        in currentNodes -> Color(0xFFFFA726)
                        in visitedNodes -> Color(0xFF66BB6A)
                        in highlightedNodes -> Color(0xFF42A5F5)
                        else -> primaryColor
                    }

                    // Внешняя обводка
                    drawCircle(
                        color = color,
                        center = node.position,
                        radius = node.radius,
                        style = Stroke(width = 3f)
                    )

                    // Внутренняя заливка
                    drawCircle(
                        color = color.copy(alpha = 0.2f),
                        center = node.position,
                        radius = node.radius
                    )

                    // ID вершины
                    val paint = android.graphics.Paint().apply {
                        this.color = when (node.id) {
                            in visitedNodes, in currentNodes -> android.graphics.Color.WHITE
                            in highlightedNodes -> android.graphics.Color.WHITE
                            else -> android.graphics.Color.BLACK
                        }
                        textSize = 24f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }

                    drawContext.canvas.nativeCanvas.drawText(
                        node.id.toString(),
                        node.position.x,
                        node.position.y + 10f,
                        paint
                    )
                }
            }
        }

        // Легенда
        if (showLegend && (currentNodes.isNotEmpty() || visitedNodes.isNotEmpty() || highlightedNodes.isNotEmpty())) {
            val legendItems = mutableListOf<LegendItem>()

            if (currentNodes.isNotEmpty()) {
                legendItems.add(LegendItem(Color(0xFFFFA726), "Текущий"))
            }
            if (visitedNodes.isNotEmpty()) {
                legendItems.add(LegendItem(Color(0xFF66BB6A), "Посещённый"))
            }
            if (highlightedNodes.isNotEmpty()) {
                legendItems.add(LegendItem(Color(0xFF42A5F5), "Выделенный"))
            }

            legendItems.add(LegendItem(primaryColor, "Обычный"))

            Legend(
                legendItems = legendItems,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Информация о состоянии обхода
        if (visitedNodes.isNotEmpty()) {
            Text(
                text = "Посещено вершин: ${visitedNodes.size}/${graph.keys.size}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun GraphLegend(
    modifier: Modifier = Modifier,
    showCurrent: Boolean = true,
    showVisited: Boolean = true,
    showHighlighted: Boolean = true,
    showNormal: Boolean = true
) {
    val primaryColor = MaterialTheme.colorScheme.primary

    val legendItems = mutableListOf<LegendItem>()

    if (showCurrent) {
        legendItems.add(LegendItem(Color(0xFFFFA726), "Текущий"))
    }
    if (showVisited) {
        legendItems.add(LegendItem(Color(0xFF66BB6A), "Посещённый"))
    }
    if (showHighlighted) {
        legendItems.add(LegendItem(Color(0xFF42A5F5), "Выделенный"))
    }
    if (showNormal) {
        legendItems.add(LegendItem(primaryColor, "Обычный"))
    }

    Legend(
        legendItems = legendItems,
        modifier = modifier
    )
}
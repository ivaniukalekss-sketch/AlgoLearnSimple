package com.ivaniuk.algolearnsimple.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.min
import kotlin.math.sqrt

@Composable
fun WeightedGraphVisualizer(
    graph: Map<Int, List<Int>>,
    edgeWeights: Map<Pair<Int, Int>, Int> = emptyMap(),
    highlightedNodes: Set<Int> = emptySet(),
    currentNodes: Set<Int> = emptySet(),
    visitedNodes: Set<Int> = emptySet(),
    highlightedEdges: Set<Pair<Int, Int>> = emptySet(),
    startNode: Int? = null,
    targetNode: Int? = null,
    modifier: Modifier = Modifier,
    size: DpSize = DpSize(350.dp, 300.dp),
    showLegend: Boolean = true,
    showDynamicLegend: Boolean = false
) {
    val density = LocalDensity.current
    val widthPx = with(density) { size.width.toPx() }
    val heightPx = with(density) { size.height.toPx() }

    val primaryColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
    ) {

        Box(
            modifier = Modifier
                .size(size)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
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

                // Рисуем рёбра с весами
                graph.forEach { (nodeId, neighbors) ->
                    val node = nodes.find { it.id == nodeId }
                    node?.let { source ->
                        neighbors.forEach { neighborId ->
                            val target = nodes.find { it.id == neighborId }
                            target?.let {
                                // Определяем цвет ребра
                                val isHighlighted = Pair(nodeId, neighborId) in highlightedEdges ||
                                        Pair(neighborId, nodeId) in highlightedEdges
                                val edgeColor = if (isHighlighted) Color.Blue else Color.Gray.copy(alpha = 0.5f)
                                val edgeWidth = if (isHighlighted) 3f else 2f

                                // Рисуем ребро
                                drawLine(
                                    color = edgeColor,
                                    start = source.position,
                                    end = target.position,
                                    strokeWidth = edgeWidth
                                )

                                // Рисуем вес ребра
                                val weight = edgeWeights[Pair(nodeId, neighborId)]
                                if (weight != null) {
                                    drawEdgeWeight(
                                        from = source.position,
                                        to = target.position,
                                        weight = weight.toString(),
                                        isHighlighted = isHighlighted
                                    )
                                }
                            }
                        }
                    }
                }

                // Рисуем вершины
                nodes.forEach { node ->
                    val color = when {
                        node.id == startNode -> Color(0xFF2196F3)        // Синий - Старт
                        node.id == targetNode -> Color(0xFFFF9800)       // Оранжевый - Цель
                        node.id in currentNodes -> Color(0xFFFFA726)     // Оранжевый - Текущий
                        node.id in visitedNodes -> Color(0xFF66BB6A)     // Зелёный - Посещённый
                        node.id in highlightedNodes -> Color(0xFF42A5F5) // Синий - Выделенный
                        else -> primaryColor                             // Основной - Обычный
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
                    drawIntoCanvas { canvas ->
                        val paint = android.graphics.Paint().apply {
                            this.color = when {
                                node.id in visitedNodes || node.id in currentNodes ||
                                        node.id == startNode || node.id == targetNode -> {
                                    android.graphics.Color.WHITE
                                }
                                node.id in highlightedNodes -> {
                                    android.graphics.Color.WHITE
                                }
                                else -> {
                                    android.graphics.Color.BLACK
                                }
                            }
                            textSize = 24f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }

                        canvas.nativeCanvas.drawText(
                            node.id.toString(),
                            node.position.x,
                            node.position.y + 10f,
                            paint
                        )
                    }
                }
            }
        }

        // Легенда для взвешенного графа
        if (showLegend) {
            val legendItems = mutableListOf<LegendItem>()

            // Фиксированные элементы для Дейкстры
            startNode?.let {
                legendItems.add(LegendItem(Color(0xFF2196F3), "Старт"))
            }
            targetNode?.let {
                legendItems.add(LegendItem(Color(0xFFFF9800), "Цель"))
            }

            // Динамические элементы (только если есть соответствующие вершины)
            val actualCurrentNodes = currentNodes.intersect(graph.keys)
            val actualVisitedNodes = visitedNodes.intersect(graph.keys)
            val actualHighlightedNodes = highlightedNodes.intersect(graph.keys)

            if (actualCurrentNodes.isNotEmpty()) {
                legendItems.add(LegendItem(Color(0xFFFFA726), "Текущая"))
            }
            if (actualVisitedNodes.isNotEmpty()) {
                legendItems.add(LegendItem(Color(0xFF66BB6A), "Обработанная"))
            }
            if (actualHighlightedNodes.isNotEmpty()) {
                legendItems.add(LegendItem(Color(0xFF42A5F5), "Рассматриваемая"))
            }
            if (highlightedEdges.isNotEmpty()) {
                legendItems.add(LegendItem(Color.Blue, "Путь", showCircle = false, showLine = true))
            }

            // Всегда добавляем базовый цвет для контекста
            legendItems.add(LegendItem(primaryColor, "Обычная"))

            // Отрисовываем легенду
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Обозначения:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Разбиваем легенду на строки по 3 элемента для лучшего отображения
                val rows = legendItems.chunked(3)

                rows.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        rowItems.forEach { item ->
                            SingleLegendItem(
                                item = item,
                                modifier = Modifier.width(80.dp)
                            )
                        }
                        // Заполняем оставшееся место если элементов меньше 3
                        repeat(3 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    if (rows.indexOf(rowItems) < rows.size - 1) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
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

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawEdgeWeight(
    from: Offset,
    to: Offset,
    weight: String,
    isHighlighted: Boolean
) {
    val midX = (from.x + to.x) / 2
    val midY = (from.y + to.y) / 2

    // Рассчитываем перпендикулярное смещение
    val perpendicular = calculatePerpendicularOffset(from, to)
    val offsetX = perpendicular.x * 20f
    val offsetY = perpendicular.y * 20f

    // Рисуем текст веса
    drawIntoCanvas { canvas ->
        val paint = android.graphics.Paint().apply {
            color = if (isHighlighted) {
                android.graphics.Color.BLUE
            } else {
                android.graphics.Color.DKGRAY
            }
            textSize = 18f
            textAlign = android.graphics.Paint.Align.CENTER
            style = android.graphics.Paint.Style.FILL
            isAntiAlias = true
            // Лёгкая тень для лучшей читаемости
            setShadowLayer(2f, 0f, 0f, android.graphics.Color.WHITE)
        }

        canvas.nativeCanvas.drawText(
            weight,
            midX + offsetX,
            midY + offsetY + 6f,
            paint
        )
    }
}

private fun calculatePerpendicularOffset(start: Offset, end: Offset): Offset {
    val dx = end.x - start.x
    val dy = end.y - start.y
    val length = sqrt(dx * dx + dy * dy)

    if (length == 0f) return Offset(0f, 0f)

    return Offset(-dy / length, dx / length)
}

@Composable
fun WeightedGraphLegend(
    modifier: Modifier = Modifier,
    showStartTarget: Boolean = true,
    showCurrent: Boolean = false,
    showVisited: Boolean = false,
    showHighlighted: Boolean = false,
    showHighlightedEdges: Boolean = false
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val legendItems = mutableListOf<LegendItem>()

    // Базовые элементы (всегда показываем)
    legendItems.add(LegendItem(Color(0xFF2196F3), "Старт"))
    legendItems.add(LegendItem(Color(0xFFFF9800), "Цель"))
    legendItems.add(LegendItem(primaryColor, "Обычная"))
    legendItems.add(LegendItem(Color.Blue, "Путь", showCircle = false, showLine = true))

    // Динамические элементы (показываем только если нужно)
    if (showCurrent) {
        legendItems.add(LegendItem(Color(0xFFFFA726), "Текущая"))
    }
    if (showVisited) {
        legendItems.add(LegendItem(Color(0xFF66BB6A), "Обработанная"))
    }
    if (showHighlighted) {
        legendItems.add(LegendItem(Color(0xFF42A5F5), "Рассматриваемая"))
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Обозначения:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Разбиваем на строки (максимум 3 элемента в строке)
        val rows = legendItems.chunked(3)

        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                rowItems.forEach { item ->
                    SingleLegendItem(
                        item = item,
                        modifier = Modifier.width(80.dp)
                    )
                }
                // Заполняем пустые места
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            // Добавляем небольшой отступ между строками
            if (rows.indexOf(rowItems) < rows.size - 1) {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun SingleLegendItem(
    item: LegendItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when {
            item.showCircle -> {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(item.color)
                )
            }
            item.showLine -> {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(3.dp)
                        .background(item.color)
                )
            }
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




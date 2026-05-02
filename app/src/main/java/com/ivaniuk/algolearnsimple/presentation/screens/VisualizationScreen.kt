package com.ivaniuk.algolearnsimple.presentation.screens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ivaniuk.algolearnsimple.domain.model.Speed
import com.ivaniuk.algolearnsimple.presentation.components.ArrayVisualizer
import com.ivaniuk.algolearnsimple.presentation.components.GraphVisualizer
import com.ivaniuk.algolearnsimple.presentation.components.GraphLegend
import com.ivaniuk.algolearnsimple.presentation.viewmodel.VisualizationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualizationScreen(
    viewModel: VisualizationViewModel,
    onBack: () -> Unit
) {
    val steps by viewModel.steps.collectAsState()
    val currentStepIndex by viewModel.currentStepIndex.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val speed by viewModel.speed.collectAsState()
    val isRandomData by viewModel.isRandomData.collectAsState()

    val currentStep = viewModel.getCurrentStep()
    LaunchedEffect(isPlaying, currentStepIndex) {
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Визуализация: ${viewModel.getAlgorithmName()}",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.generateRandomData() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = "Случайные данные",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        bottomBar = {
            VisualizationControls(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (isRandomData) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Используются случайные данные",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        TextButton(
                            onClick = { viewModel.resetToDefaultData() }
                        ) {
                            Text("Сбросить")
                        }
                    }
                }
            }
            StepProgress(
                currentStep = currentStepIndex,
                totalSteps = steps.size,
                onStepSelected = { index ->
                    viewModel.setStep(index)
                }
            )

            if (isPlaying) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Шаг ${currentStepIndex + 1} из ${steps.size}",
                            style = MaterialTheme.typography.titleSmall
                        )

                        Text(
                            text = "Скорость: ${when (speed) {
                                Speed.SLOW -> "🐌 Медленно"
                                Speed.MEDIUM -> "🚶 Средне"
                                Speed.FAST -> "🏃 Быстро"
                                Speed.VERY_FAST -> "⚡ Очень быстро"
                            }}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (currentStep != null) {
                if (currentStep.array != null) {

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ArrayVisualizer(
                                array = currentStep.array,
                                highlightedIndices = currentStep.highlightedIndices,
                                comparingIndices = currentStep.comparingIndices,
                                swappedIndices = currentStep.swappedIndices,
                                sortedIndices = currentStep.sortedIndices,
                                currentIndex = currentStep.currentIndex,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(460.dp)
                            )

                        }
                    }
                }


                if (currentStep.graph != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (viewModel.getAlgorithmName() == "BFS (Breadth-First Search)") {
                                    "Граф (Поиск в ширину)"
                                } else {
                                    "Граф"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            GraphVisualizer(
                                graph = currentStep.graph,
                                highlightedNodes = currentStep.highlightedIndices,
                                currentNodes = currentStep.comparingIndices,
                                visitedNodes = currentStep.sortedIndices,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            GraphLegend(modifier = Modifier.fillMaxWidth())



                            if (viewModel.getAlgorithmName() == "BFS (Breadth-First Search)" &&
                                currentStep.customData != null) {
                                Spacer(modifier = Modifier.height(12.dp))

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Text(
                                            text = "Уровни вершин (от стартовой):",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }


                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Шаг ${currentStep.stepNumber}:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentStep.description,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify
                        )

                        currentStep.codeLine?.let { codeLine ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Text(
                                    text = codeLine,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Скорость:",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Text(
                        text = when (speed) {
                            Speed.SLOW -> "Медленно"
                            Speed.MEDIUM -> "Средне"
                            Speed.FAST -> "Быстро"
                            Speed.VERY_FAST -> "Очень быстро"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Загрузка визуализации...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Информация:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = viewModel.getInputDescription(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


@Composable
fun StepProgress(
    currentStep: Int,
    totalSteps: Int,
    onStepSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Прогресс:",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "$currentStep / ${totalSteps.coerceAtLeast(1)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = currentStep.toFloat(),
                onValueChange = { newValue ->
                    onStepSelected(newValue.toInt())
                },
                valueRange = 0f..totalSteps.coerceAtLeast(1).toFloat(),
                steps = totalSteps.coerceAtLeast(1) - 1
            )
        }
    }
}

@Composable
fun VisualizationControls(
    viewModel: VisualizationViewModel,
    modifier: Modifier = Modifier
) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val speed by viewModel.speed.collectAsState()

    Surface(
        tonalElevation = 8.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(
                    onClick = { viewModel.resetToFirstStep() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "В начало")
                }

                FilledTonalIconButton(
                    onClick = { viewModel.previousStep() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Предыдущий шаг")
                }

                FilledIconButton(
                    onClick = { viewModel.togglePlayPause() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Пауза" else "Старт"
                    )
                }

                FilledTonalIconButton(
                    onClick = { viewModel.nextStep() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Следующий шаг")
                }

                FilledTonalIconButton(
                    onClick = { viewModel.goToLastStep() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.SkipNext, contentDescription = "В конец")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Скорость:",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f)
                )

                Speed.entries.forEach { speedOption ->
                    FilterChip(
                        selected = speed == speedOption,
                        onClick = { viewModel.setSpeed(speedOption) },
                        label = {
                            Text(
                                text = when (speedOption) {
                                    Speed.SLOW -> "🐌"
                                    Speed.MEDIUM -> "🚶"
                                    Speed.FAST -> "🏃"
                                    Speed.VERY_FAST -> "⚡"
                                }
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
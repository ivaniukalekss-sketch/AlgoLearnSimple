package com.ivaniuk.algolearnsimple.presentation.components

import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ivaniuk.algolearnsimple.domain.model.Algorithm

@Composable
fun AlgorithmCard(
    algorithm: Algorithm,
    onCardClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    onVisualizeClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = algorithm.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Row {
                    // Кнопка визуализации
                    if (onVisualizeClick != null) {
                        IconButton(onClick = onVisualizeClick) {
                            Icon(
                                imageVector = Icons.Default.PlayCircleOutline,
                                contentDescription = "Визуализировать",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Кнопка избранного
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (algorithm.isFavorite) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = if (algorithm.isFavorite) {
                                "Удалить из избранного"
                            } else {
                                "Добавить в избранное"
                            },
                            tint = if (algorithm.isFavorite) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
                }

            Text(
                text = algorithm.description.take(100) + "...",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Категория: ${algorithm.category.name}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Сложность: ${algorithm.complexity}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
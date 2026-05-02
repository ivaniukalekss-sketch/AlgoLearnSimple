package com.ivaniuk.algolearnsimple.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.TopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialScreen(
    onComplete: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }

    val pages = listOf(
        TutorialPage(
            title = "Добро пожаловать в AlgoLearn! 👋",
            description = "Интерактивное приложение для изучения алгоритмов с пошаговой визуализацией.",
            emoji = "🎓",
            color = MaterialTheme.colorScheme.primaryContainer
        ),
        TutorialPage(
            title = "Выбери алгоритм",
            description = "На главном экране ты найдёшь 10 алгоритмов. Нажми на карточку, чтобы узнать подробности.",
            emoji = "📱",
            color = MaterialTheme.colorScheme.secondaryContainer
        ),
        TutorialPage(
            title = "Запусти визуализацию",
            description = "Нажми на кнопку ▶️, чтобы увидеть алгоритм в действии. Управляй скоростью и шагами!",
            emoji = "▶️",
            color = MaterialTheme.colorScheme.tertiaryContainer
        ),
        TutorialPage(
            title = "Добавляй в избранное",
            description = "Нажми на ❤️, чтобы сохранить алгоритм в избранное. Все избранные алгоритмы будут в отдельном разделе.",
            emoji = "❤️",
            color = MaterialTheme.colorScheme.primaryContainer
        ),
        TutorialPage(
            title = "Следи за прогрессом",
            description = "В профиле ты видишь свои достижения и статистику. Чем больше изучаешь — тем больше открываешь!",
            emoji = "🏆",
            color = MaterialTheme.colorScheme.secondaryContainer
        ),
        TutorialPage(
            title = "Готов к обучению? 🚀",
            description = "Начни изучать алгоритмы прямо сейчас! Удачи в освоении!",
            emoji = "🎯",
            color = MaterialTheme.colorScheme.tertiaryContainer
        )
    )

    Scaffold(
        containerColor = pages[currentPage].color,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = { },
                actions = {
                    IconButton(onClick = onComplete) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Пропустить",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = pages[currentPage].color.copy(alpha = 0f)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pages.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == currentPage) 10.dp else 6.dp)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (index == currentPage)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                else
                                    Color.LightGray
                            )
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = pages[currentPage].emoji,
                    fontSize = 80.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = pages[currentPage].title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = pages[currentPage].description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentPage > 0) {
                    Button(
                        onClick = { currentPage-- },
                        colors = ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text("◀ Назад")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Button(
                    onClick = {
                        if (currentPage < pages.size - 1) {
                            currentPage++
                        } else {
                            onComplete()
                        }
                    }
                ) {
                    Text(if (currentPage < pages.size - 1) "Далее ▶" else "Начать обучение! 🚀")
                }
            }
        }
    }
}

data class TutorialPage(
    val title: String,
    val description: String,
    val emoji: String,
    val color: Color
)
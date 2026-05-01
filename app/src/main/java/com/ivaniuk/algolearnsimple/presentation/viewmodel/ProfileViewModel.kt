package com.ivaniuk.algolearnsimple.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AchievementItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AlgorithmRepository
) : ViewModel() {

    private val _viewedCount = MutableStateFlow(0)
    val viewedCount: StateFlow<Int> = _viewedCount.asStateFlow()

    private val _favoriteCount = MutableStateFlow(0)
    val favoriteCount: StateFlow<Int> = _favoriteCount.asStateFlow()

    private val _achievements = MutableStateFlow<List<AchievementItem>>(emptyList())
    val achievements: StateFlow<List<AchievementItem>> = _achievements.asStateFlow()

    fun loadStats() {
        viewModelScope.launch {
            val algorithms = repository.getAllAlgorithms().collect { list ->
                _favoriteCount.value = list.count { it.isFavorite }
            }
        }

        // Временное решение для просмотров
        viewModelScope.launch {
            repository.getFavoriteAlgorithms().collect { favorites ->
                _favoriteCount.value = favorites.size
            }
        }
    }

    fun loadAchievements() {
        val viewedCountValue = _viewedCount.value

        val achievementsList = listOf(
            AchievementItem(
                id = "first_view",
                title = "🌱 Новичок",
                description = "Просмотрел первый алгоритм",
                icon = "🌱",
                isUnlocked = viewedCountValue >= 1
            ),
            AchievementItem(
                id = "three_views",
                title = "📚 Студент",
                description = "Просмотрел 3 алгоритма",
                icon = "📚",
                isUnlocked = viewedCountValue >= 3
            ),
            AchievementItem(
                id = "five_views",
                title = "🧠 Знаток",
                description = "Просмотрел 5 алгоритмов",
                icon = "🧠",
                isUnlocked = viewedCountValue >= 5
            ),
            AchievementItem(
                id = "all_views",
                title = "⭐ Мастер",
                description = "Просмотрел все 7 алгоритмов",
                icon = "⭐",
                isUnlocked = viewedCountValue >= 7
            ),
            AchievementItem(
                id = "first_favorite",
                title = "❤️ Первое избранное",
                description = "Добавил алгоритм в избранное",
                icon = "❤️",
                isUnlocked = _favoriteCount.value >= 1
            )
        )

        _achievements.value = achievementsList
    }
}
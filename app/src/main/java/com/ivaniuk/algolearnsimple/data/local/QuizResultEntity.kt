package com.ivaniuk.algolearnsimple.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val algorithmId: Int,
    val score: Int,                 // количество правильных ответов
    val totalQuestions: Int,       // всего вопросов
    val percentage: Int,           // процент правильных (0-100)
    val timestamp: Long = System.currentTimeMillis(),
    val isPassed: Boolean = false  // сдано или нет (обычно 70%+)
)
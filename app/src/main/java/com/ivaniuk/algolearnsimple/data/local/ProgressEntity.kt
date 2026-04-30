package com.ivaniuk.algolearnsimple.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress")
data class ProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val algorithmId: Int,
    val isViewed: Boolean = false,           // просмотрен ли алгоритм
    val isQuizCompleted: Boolean = false,    // пройдена ли викторина
    val quizScore: Int = 0,                  // лучший результат
    val viewCount: Int = 0,                  // сколько раз просмотрен
    val lastViewed: Long = 0,                // последний просмотр
    val completedDate: Long = 0              // когда завершён
)
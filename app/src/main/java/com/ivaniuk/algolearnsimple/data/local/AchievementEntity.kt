package com.ivaniuk.algolearnsimple.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val achievementId: String,      // уникальный ID достижения
    val title: String,              // название
    val description: String,        // описание
    val icon: String,               // эмодзи или название иконки
    val condition: String,          // условие получения
    val isUnlocked: Boolean = false,
    val unlockedDate: Long = 0
)
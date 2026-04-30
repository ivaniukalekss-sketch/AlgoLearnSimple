package com.ivaniuk.algolearnsimple.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val algorithmId: Int,
    val algorithmTitle: String,
    val timestamp: Long = System.currentTimeMillis()
)
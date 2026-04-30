package com.ivaniuk.algolearnsimple.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_questions")
data class QuizQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val algorithmId: Int,           // к какому алгоритму относится вопрос
    val question: String,           // текст вопроса
    val option1: String,            // вариант ответа 1
    val option2: String,            // вариант ответа 2
    val option3: String,            // вариант ответа 3
    val option4: String,            // вариант ответа 4
    val correctOption: Int,         // номер правильного ответа (1-4)
    val explanation: String         // пояснение после ответа
)
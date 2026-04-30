package com.ivaniuk.algolearnsimple.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    // Вопросы
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuizQuestionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestionEntity>)

    @Query("SELECT * FROM quiz_questions WHERE algorithmId = :algorithmId")
    fun getQuestionsForAlgorithm(algorithmId: Int): Flow<List<QuizQuestionEntity>>

    @Query("SELECT COUNT(*) FROM quiz_questions WHERE algorithmId = :algorithmId")
    suspend fun getQuestionCountForAlgorithm(algorithmId: Int): Int

    // Результаты
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: QuizResultEntity)

    @Query("SELECT * FROM quiz_results WHERE algorithmId = :algorithmId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastResultForAlgorithm(algorithmId: Int): QuizResultEntity?

    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<QuizResultEntity>>

    // Прогресс
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: ProgressEntity)

    @Query("SELECT * FROM progress WHERE algorithmId = :algorithmId")
    suspend fun getProgressForAlgorithm(algorithmId: Int): ProgressEntity?

    @Query("SELECT * FROM progress")
    fun getAllProgress(): Flow<List<ProgressEntity>>

    @Query("UPDATE progress SET isViewed = 1, viewCount = viewCount + 1, lastViewed = :timestamp WHERE algorithmId = :algorithmId")
    suspend fun markAlgorithmViewed(algorithmId: Int, timestamp: Long)

    @Query("UPDATE progress SET isQuizCompleted = 1, quizScore = :score, completedDate = :timestamp WHERE algorithmId = :algorithmId")
    suspend fun markQuizCompleted(algorithmId: Int, score: Int, timestamp: Long)

    // Достижения
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: AchievementEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Query("SELECT * FROM achievements WHERE isUnlocked = 1 ORDER BY unlockedDate DESC")
    fun getUnlockedAchievements(): Flow<List<AchievementEntity>>

    @Query("SELECT * FROM achievements")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Query("UPDATE achievements SET isUnlocked = 1, unlockedDate = :timestamp WHERE achievementId = :achievementId")
    suspend fun unlockAchievement(achievementId: String, timestamp: Long)

    @Query("SELECT * FROM achievements WHERE achievementId = :achievementId")
    suspend fun getAchievementById(achievementId: String): AchievementEntity?
}
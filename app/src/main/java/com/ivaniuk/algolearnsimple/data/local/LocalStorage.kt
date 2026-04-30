package com.ivaniuk.algolearnsimple.data.local

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow

class LocalStorage(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        "algo_learn_prefs",
        Context.MODE_PRIVATE
    )

    private val database = HistoryDatabase.getDatabase(context)
    private val historyDao = database.historyDao()

    private val favoritesKey = "favorite_algorithms"

    fun saveFavorites(favoriteIds: Set<Int>) {
        val stringSet = favoriteIds.map { it.toString() }.toSet()
        sharedPreferences.edit {
            putStringSet(favoritesKey, stringSet)
        }
    }

    fun getFavorites(): Set<Int> {
        val stringSet = sharedPreferences.getStringSet(favoritesKey, emptySet()) ?: emptySet()
        return stringSet.mapNotNull { it.toIntOrNull() }.toSet()
    }




    fun getRecentHistory(): Flow<List<HistoryEntity>> {
        return historyDao.getRecentHistory()
    }

    suspend fun clearHistory() {
        historyDao.clearAll()
    }

    suspend fun getHistoryCount(): Int {
        return historyDao.getHistoryCount()
    }

    suspend fun addToHistory(algorithmId: Int, algorithmTitle: String) {
        val entity = HistoryEntity(
            algorithmId = algorithmId,
            algorithmTitle = algorithmTitle
        )
        historyDao.insert(entity)
    }
}
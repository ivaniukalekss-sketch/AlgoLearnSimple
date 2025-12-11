package com.ivaniuk.algolearnsimple.data.local

import android.content.Context
import androidx.core.content.edit

class LocalStorage(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        "algo_learn_prefs",
        Context.MODE_PRIVATE
    )

    private val FAVORITES_KEY = "favorite_algorithms"

    fun saveFavorites(favoriteIds: Set<Int>) {
        // Конвертируем Set<Int> в Set<String> для SharedPreferences
        val stringSet = favoriteIds.map { it.toString() }.toSet()
        sharedPreferences.edit {
            putStringSet(FAVORITES_KEY, stringSet)
        }
    }

    fun getFavorites(): Set<Int> {
        val stringSet = sharedPreferences.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        // Конвертируем Set<String> обратно в Set<Int>
        return stringSet.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun clearFavorites() {
        sharedPreferences.edit {
            remove(FAVORITES_KEY)
        }
    }
    fun saveStudyStatistic(statisticJson: String) {
        val stats = getStudyStatistics().toMutableList()
        stats.add(statisticJson)
        sharedPreferences.edit {
            putStringSet(STUDY_STATS_KEY, stats.toSet())
        }
    }

    fun getStudyStatistics(): Set<String> {
        return sharedPreferences.getStringSet(STUDY_STATS_KEY, emptySet()) ?: emptySet()
    }

    fun saveUserStats(statsJson: String) {
        sharedPreferences.edit {
            putString(USER_STATS_KEY, statsJson)
        }
    }

    fun getUserStats(): String? {
        return sharedPreferences.getString(USER_STATS_KEY, null)
    }

    fun saveString(key: String, value: String) {
        sharedPreferences.edit {
            putString(key, value)
        }
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit {
            putInt(key, value)
        }
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        // Существующие ключи
        const val FAVORITES_KEY = "favorite_algorithms"

        // Новые ключи для статистики
        const val STUDY_STATS_KEY = "study_statistics"
        const val USER_STATS_KEY = "user_statistics"
        const val DAILY_STATS_KEY = "daily_statistics"
        const val LEARNING_STREAK_KEY = "learning_streak"
        const val LAST_STUDY_DATE_KEY = "last_study_date"
    }
}
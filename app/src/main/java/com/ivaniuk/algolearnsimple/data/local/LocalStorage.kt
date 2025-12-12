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
        const val FAVORITES_KEY = "favorite_algorithms"
    }
}
package com.ivaniuk.algolearnsimple.data.local

import android.content.Context
import androidx.core.content.edit

class LocalStorage(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        "algo_learn_prefs",
        Context.MODE_PRIVATE
    )

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
}
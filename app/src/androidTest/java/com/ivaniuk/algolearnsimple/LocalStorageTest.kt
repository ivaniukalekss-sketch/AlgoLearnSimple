package com.ivaniuk.algolearnsimple.data.local

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class LocalStorageTest {

    private lateinit var localStorage: LocalStorage
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        localStorage = LocalStorage(context)

        clearSharedPreferences()
    }

    @After
    fun cleanup() {
        clearSharedPreferences()
    }

    private fun clearSharedPreferences() {
        context.getSharedPreferences("algo_learn_prefs", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    @Test
    fun testSaveAndGetFavorites() {
        val favoriteIds = setOf(1, 2, 3)

        localStorage.saveFavorites(favoriteIds)
        val retrievedIds = localStorage.getFavorites()

        assertEquals(favoriteIds, retrievedIds)
    }

    @Test
    fun testGetFavoritesReturnsEmptySetWhenNoneSaved() {

        val favorites = localStorage.getFavorites()

        assertTrue(favorites.isEmpty())
    }

    @Test
    fun testSaveFavoritesReplacesPreviousValues() {
        val firstSet = setOf(1, 2)
        val secondSet = setOf(3, 4)

        localStorage.saveFavorites(firstSet)
        localStorage.saveFavorites(secondSet)
        val retrievedIds = localStorage.getFavorites()

        assertEquals(secondSet, retrievedIds)
        assertFalse(retrievedIds.contains(1))
    }
}
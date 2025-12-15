package com.ivaniuk.algolearnsimple.data.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ivaniuk.algolearnsimple.domain.model.AlgorithmCategory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class AlgorithmRepositoryImplTest {

    private lateinit var repository: AlgorithmRepositoryImpl
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        repository = AlgorithmRepositoryImpl(context)

        clearSharedPreferences()
    }

    @After
    fun cleanup() {
        clearSharedPreferences()
    }

    private fun clearSharedPreferences() {
        val prefs = context.getSharedPreferences(
            "algo_learn_prefs",
            Context.MODE_PRIVATE
        )
        prefs.edit().clear().apply()
    }

    @Test
    fun testGetAllAlgorithmsReturnsCorrectList() = runBlocking {

        val expectedCount = 5

        val algorithms = repository.getAllAlgorithms().first()

        assertEquals(expectedCount, algorithms.size)
    }

    @Test
    fun testGetAlgorithmByIdReturnsCorrectAlgorithm() = runBlocking {
        val expectedId = 1
        val expectedTitle = "Bubble Sort"

        val algorithm = repository.getAlgorithmById(expectedId)

        assertNotNull(algorithm)
        assertEquals(expectedId, algorithm?.id)
        assertEquals(expectedTitle, algorithm?.title)
    }

    @Test
    fun testGetAlgorithmByIdReturnsNullForNonExistentId() = runBlocking {
        val nonExistentId = 999

        val algorithm = repository.getAlgorithmById(nonExistentId)

        assertNull(algorithm)
    }

    @Test
    fun testToggleFavoriteChangesFavoriteStatus() = runBlocking {

        val algorithmId = 1

        val algorithmBefore = repository.getAlgorithmById(algorithmId)
        val isFavoriteBefore = algorithmBefore?.isFavorite ?: false

        repository.toggleFavorite(algorithmId)

        val algorithmAfter = repository.getAlgorithmById(algorithmId)
        val isFavoriteAfter = algorithmAfter?.isFavorite ?: false

        assertNotEquals(isFavoriteBefore, isFavoriteAfter)
    }

    @Test
    fun testGetAlgorithmsByCategoryReturnsCorrectCategory() = runBlocking {
        val category = AlgorithmCategory.SORTING.name

        val algorithms = repository.getAlgorithmsByCategory(category).first()

        assertTrue(algorithms.isNotEmpty())
        assertTrue(algorithms.all { it.category.name == category })
    }

    @Test
    fun testGetFavoriteAlgorithmsReturnsOnlyFavorites() = runBlocking {

        repository.toggleFavorite(1)

        val favorites = repository.getFavoriteAlgorithms().first()

        assertTrue(favorites.isNotEmpty())
        assertTrue(favorites.all { it.isFavorite })
    }
}
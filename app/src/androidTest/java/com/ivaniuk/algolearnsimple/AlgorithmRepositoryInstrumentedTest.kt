package com.ivaniuk.algolearnsimple

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ivaniuk.algolearnsimple.data.local.LocalStorage
import com.ivaniuk.algolearnsimple.data.repository.AlgorithmRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlgorithmRepositoryInstrumentedTest {

    private lateinit var repository: AlgorithmRepositoryImpl
    private lateinit var localStorage: LocalStorage

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        localStorage = LocalStorage(context)
        repository = AlgorithmRepositoryImpl(localStorage)
    }

    @Test
    fun testRepositoryPersistsFavorites() = runBlocking {
        val algorithmId = 1

        repository.toggleFavorite(algorithmId)

        val context: Context = ApplicationProvider.getApplicationContext()
        val newLocalStorage = LocalStorage(context)
        val newRepository = AlgorithmRepositoryImpl(newLocalStorage)

        val algorithm = newRepository.getAlgorithmById(algorithmId)

        assertNotNull(algorithm)
        assertTrue(algorithm?.isFavorite ?: false)
    }

    @Test
    fun testGetAllAlgorithmsReturnsData() = runBlocking {
        val algorithms = repository.getAllAlgorithms()
            .first { it.isNotEmpty() }

        assertEquals(10, algorithms.size)  // Теперь 10 алгоритмов
        assertEquals("Bubble Sort", algorithms[0].title)
        assertEquals("Binary Search", algorithms[1].title)
        assertEquals("Quick Sort", algorithms[2].title)
        assertEquals("DFS (Depth-First Search)", algorithms[3].title)
        assertEquals("BFS (Breadth-First Search)", algorithms[4].title)
        assertEquals("Merge Sort", algorithms[5].title)
        assertEquals("Dijkstra", algorithms[6].title)
        assertEquals("Selection Sort", algorithms[7].title)
        assertEquals("Insertion Sort", algorithms[8].title)
        assertEquals("Linear Search", algorithms[9].title)
    }

    @Test
    fun testToggleFavoriteUpdatesImmediately() = runBlocking {
        val algorithmId = 2

        val algorithmBefore = repository.getAlgorithmById(algorithmId)
        val isFavoriteBefore = algorithmBefore?.isFavorite ?: false

        repository.toggleFavorite(algorithmId)

        val algorithmAfter = repository.getAlgorithmById(algorithmId)

        assertNotNull(algorithmAfter)
        assertEquals(!isFavoriteBefore, algorithmAfter?.isFavorite)
    }

    @Test
    fun testGetFavoriteAlgorithmsFlow() = runBlocking {
        repository.toggleFavorite(1)
        repository.toggleFavorite(3)
        repository.toggleFavorite(5)

        val favorites = repository.getFavoriteAlgorithms().first()

        assertTrue(favorites.isNotEmpty())
        assertEquals(3, favorites.size)
        assertTrue(favorites.all { it.isFavorite })
    }

    @Test
    fun testGetAlgorithmsByCategory() = runBlocking {
        val category = "SORTING"

        val algorithms = repository.getAlgorithmsByCategory(category).first()

        assertEquals(5, algorithms.size)
        assertTrue(algorithms.all { it.category.name == category })
    }

    @Test
    fun testGetNonExistentAlgorithm() = runBlocking {
        val nonExistentId = 999

        val algorithm = repository.getAlgorithmById(nonExistentId)

        assertNull(algorithm)
    }

    @Test
    fun testClearFavorites() = runBlocking {
        repository.toggleFavorite(1)

        val afterAdd = repository.getFavoriteAlgorithms().first()
        assertEquals(1, afterAdd.size)
        assertTrue(afterAdd[0].isFavorite)

        repository.toggleFavorite(1)

        val afterRemove = repository.getFavoriteAlgorithms().first()
        assertTrue(afterRemove.isEmpty())
    }
}
package com.ivaniuk.algolearnsimple.integration

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ivaniuk.algolearnsimple.data.local.HistoryDatabase
import com.ivaniuk.algolearnsimple.data.local.LocalStorage
import com.ivaniuk.algolearnsimple.data.repository.AlgorithmRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryIntegrationTest {

    private lateinit var repository: AlgorithmRepositoryImpl
    private lateinit var localStorage: LocalStorage
    private lateinit var database: HistoryDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = HistoryDatabase.getDatabase(context)
        localStorage = LocalStorage(context)
        repository = AlgorithmRepositoryImpl(localStorage)
    }

    @After
    fun cleanup() = runBlocking {
        database.historyDao().clearAll()
        val prefs = ApplicationProvider.getApplicationContext<android.content.Context>()
            .getSharedPreferences("algo_learn_prefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    @Test
    fun testAddToHistoryAndRetrieve() = runBlocking {
        val algorithmId = 1
        val algorithmTitle = "Bubble Sort"

        repository.getAlgorithmById(algorithmId)

        val history = localStorage.getRecentHistory().first()
        assertTrue(history.any { it.algorithmId == algorithmId })
    }

    @Test
    fun testToggleFavoritePersists() = runBlocking {
        val algorithmId = 1

        repository.toggleFavorite(algorithmId)
        val algorithm = repository.getAlgorithmById(algorithmId)

        assertTrue(algorithm?.isFavorite == true)

        repository.toggleFavorite(algorithmId)
        val algorithmAfter = repository.getAlgorithmById(algorithmId)

        assertTrue(algorithmAfter?.isFavorite == false)
    }
}
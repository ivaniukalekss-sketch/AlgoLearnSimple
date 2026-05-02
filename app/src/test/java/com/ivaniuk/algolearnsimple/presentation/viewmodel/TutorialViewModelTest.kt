package com.ivaniuk.algolearnsimple.presentation.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class TutorialViewModelTest {

    private lateinit var viewModel: TutorialViewModel
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        viewModel = TutorialViewModel(context)

        val prefs = context.getSharedPreferences("tutorial", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    @Test
    fun testIsTutorialSeenReturnsFalseByDefault() {
        assertFalse(viewModel.isTutorialSeen())
    }

    @Test
    fun testSetTutorialSeenMakesIsTutorialSeenTrue() {
        viewModel.setTutorialSeen()
        assertTrue(viewModel.isTutorialSeen())
    }
}
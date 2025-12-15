package com.ivaniuk.algolearnsimple

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BasicUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testActivityLaunches() {
        Thread.sleep(1000)
    }

    @Test
    fun testNoCrashes() {
        Thread.sleep(2000)
        assert(true)
    }
}
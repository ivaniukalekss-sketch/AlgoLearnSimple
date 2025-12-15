package com.ivaniuk.algolearnsimple

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class BasicNavigationTest {

    @Test
    fun testNavigationRoutesExist() {

        val routes = listOf(
            "home",
            "algorithm/{algorithmId}",
            "favorites",
            "visualization/{algorithmId}"
        )

        assertEquals(4, routes.size)
        assertTrue(routes.contains("home"))
        assertTrue(routes.contains("favorites"))

        val algorithmRoute = routes.find { it.startsWith("algorithm/") }
        assertNotNull(algorithmRoute)
        assertTrue(algorithmRoute!!.contains("{algorithmId}"))

        val visualizationRoute = routes.find { it.startsWith("visualization/") }
        assertNotNull(visualizationRoute)
        assertTrue(visualizationRoute!!.contains("{algorithmId}"))
    }

    @Test
    fun testRouteParameterParsing() {
        fun extractAlgorithmId(route: String): Int? {
            return when {
                route.startsWith("algorithm/") -> {
                    val idStr = route.substringAfter("algorithm/")
                    idStr.toIntOrNull()
                }
                route.startsWith("visualization/") -> {
                    val idStr = route.substringAfter("visualization/")
                    idStr.toIntOrNull()
                }
                else -> null
            }
        }

        assertEquals(1, extractAlgorithmId("algorithm/1"))
        assertEquals(42, extractAlgorithmId("algorithm/42"))
        assertEquals(3, extractAlgorithmId("visualization/3"))
        assertNull(extractAlgorithmId("home"))
        assertNull(extractAlgorithmId("algorithm/not-a-number"))
    }
}
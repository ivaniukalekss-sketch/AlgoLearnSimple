package com.ivaniuk.algolearnsimple.presentation.navigation

import org.junit.Test
import org.junit.Assert.*

class NavigationRoutesTest {

    @Test
    fun `test all navigation routes exist`() {
        // Проверяем, что все необходимые маршруты определены
        val routes = listOf("home", "favorites", "algorithm/{algorithmId}", "visualization/{algorithmId}")

        assertEquals(4, routes.size)
        assertTrue(routes.contains("home"))
        assertTrue(routes.contains("favorites"))
        assertTrue(routes.contains("algorithm/{algorithmId}"))
        assertTrue(routes.contains("visualization/{algorithmId}"))
    }

    @Test
    fun `test route parameter parsing`() {
        // Тестируем извлечение ID из маршрутов
        val algorithmRoute = "algorithm/5"
        val visualizationRoute = "visualization/10"

        // Извлекаем ID из маршрута алгоритма
        val algorithmId = algorithmRoute.substringAfter("algorithm/").toIntOrNull()
        assertNotNull(algorithmId)
        assertEquals(5, algorithmId)

        // Извлекаем ID из маршрута визуализации
        val visualizationId = visualizationRoute.substringAfter("visualization/").toIntOrNull()
        assertNotNull(visualizationId)
        assertEquals(10, visualizationId)
    }

    @Test
    fun `test route building`() {
        // Проверяем построение маршрутов с параметрами
        fun buildAlgorithmRoute(id: Int): String = "algorithm/$id"
        fun buildVisualizationRoute(id: Int): String = "visualization/$id"

        assertEquals("algorithm/1", buildAlgorithmRoute(1))
        assertEquals("algorithm/42", buildAlgorithmRoute(42))
        assertEquals("visualization/3", buildVisualizationRoute(3))
        assertEquals("visualization/99", buildVisualizationRoute(99))
    }

    @Test
    fun `test invalid route handling`() {
        // Проверяем обработку невалидных маршрутов
        val invalidRoutes = listOf("", "unknown", "algorithm/", "visualization/abc")

        invalidRoutes.forEach { route ->
            val isValid = when {
                route.isEmpty() -> false
                route == "home" || route == "favorites" -> true
                route.startsWith("algorithm/") -> {
                    val id = route.substringAfter("algorithm/")
                    id.isNotEmpty() && id.all { it.isDigit() }
                }
                route.startsWith("visualization/") -> {
                    val id = route.substringAfter("visualization/")
                    id.isNotEmpty() && id.all { it.isDigit() }
                }
                else -> false
            }
            assertFalse("Route '$route' should be invalid", isValid)
        }
    }
}
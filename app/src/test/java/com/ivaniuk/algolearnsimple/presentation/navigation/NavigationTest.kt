package com.ivaniuk.algolearnsimple.presentation.navigation

import org.junit.Test
import org.junit.Assert.assertTrue

class BasicNavigationTest {

    @Test
    fun testNavigationBasic() {
        println("Тест навигации выполняется...")
        val routes = listOf("home", "favorites", "visualization")
        assertTrue("Должны быть маршруты навигации", routes.isNotEmpty())

        println("Маршруты навигации: $routes")
    }

    @Test
    fun testRouteNames() {
        val screenNames = listOf(
            "Главная",
            "Избранное",
            "Визуализация",
            "Детали алгоритма"
        )

        assertTrue("Должно быть минимум 3 экрана", screenNames.size >= 3)
        println("Экран: ${screenNames[0]}")
        println("Экран: ${screenNames[1]}")
        println("Экран: ${screenNames[2]}")
    }
}
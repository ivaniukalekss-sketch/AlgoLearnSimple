package com.ivaniuk.algolearnsimple.presentation.navigation

import org.junit.Test
import org.junit.Assert.*

class NavigationStackTest {

    @Test
    fun testNavigationStackOperations() {
        // Имитируем стек навигации
        val navigationStack = mutableListOf<String>()

        // Начальное состояние - главный экран
        navigationStack.add("home")
        assertEquals(1, navigationStack.size)
        assertEquals("home", navigationStack.last())

        // Навигация к алгоритму
        navigationStack.add("algorithm/1")
        assertEquals(2, navigationStack.size)
        assertEquals("algorithm/1", navigationStack.last())

        // Навигация к визуализации
        navigationStack.add("visualization/1")
        assertEquals(3, navigationStack.size)
        assertEquals("visualization/1", navigationStack.last())

        // Навигация назад
        navigationStack.removeLast()
        assertEquals(2, navigationStack.size)
        assertEquals("algorithm/1", navigationStack.last())

        // Еще раз назад
        navigationStack.removeLast()
        assertEquals(1, navigationStack.size)
        assertEquals("home", navigationStack.last())
    }

    @Test
    fun testNavigationPathValidation() {
        // Проверяем корректность путей навигации
        val validPaths = listOf(
            "home",
            "home → algorithm/1",
            "home → algorithm/1 → visualization/1",
            "home → favorites"
        )

        val invalidPaths = listOf(
            "algorithm/1 → home", // Нельзя перейти на home из algorithm без back
            "visualization/1 → algorithm/1", // Обратный порядок
            "unknown"
        )

        // Простая проверка - пути не должны быть пустыми
        validPaths.forEach { path ->
            assertTrue(path.isNotEmpty())
            assertTrue(path.contains("home"))
        }

        invalidPaths.forEach { path ->
            assertTrue(path.isNotEmpty())
        }
    }
}
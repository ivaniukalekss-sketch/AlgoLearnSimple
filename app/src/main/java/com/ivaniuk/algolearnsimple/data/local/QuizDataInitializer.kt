package com.ivaniuk.algolearnsimple.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object QuizDataInitializer {

    suspend fun initializeData(dao: QuizDao) = withContext(Dispatchers.IO) {
        // Проверяем, есть ли уже вопросы
        val count = dao.getQuestionCountForAlgorithm(1)
        if (count > 0) return@withContext

        // ============= ВОПРОСЫ ДЛЯ ВИКТОРИН =============
        val allQuestions = listOf(
            // ===== Bubble Sort (id=1) =====
            QuizQuestionEntity(
                algorithmId = 1,
                question = "Какая временная сложность у Bubble Sort в худшем случае?",
                option1 = "O(n)",
                option2 = "O(n log n)",
                option3 = "O(n²)",
                option4 = "O(log n)",
                correctOption = 3,
                explanation = "Bubble Sort в худшем случае делает n² сравнений и обменов."
            ),
            QuizQuestionEntity(
                algorithmId = 1,
                question = "Что делает Bubble Sort на каждом проходе?",
                option1 = "Находит наименьший элемент",
                option2 = "Наибольший элемент 'всплывает' в конец",
                option3 = "Делит массив пополам",
                option4 = "Ищет элемент по значению",
                correctOption = 2,
                explanation = "На каждом проходе наибольший элемент перемещается в конец массива."
            ),
            QuizQuestionEntity(
                algorithmId = 1,
                question = "Когда Bubble Sort эффективен?",
                option1 = "На больших массивах",
                option2 = "На почти отсортированных массивах",
                option3 = "На полностью случайных массивах",
                option4 = "Никогда",
                correctOption = 2,
                explanation = "На почти отсортированных массивах Bubble Sort может завершиться досрочно."
            ),

            // ===== Binary Search (id=2) =====
            QuizQuestionEntity(
                algorithmId = 2,
                question = "Какое главное требование для применения бинарного поиска?",
                option1 = "Массив должен быть отсортирован",
                option2 = "Массив должен быть большим",
                option3 = "Массив должен содержать только числа",
                option4 = "Массив должен быть неотсортирован",
                correctOption = 1,
                explanation = "Бинарный поиск работает только на отсортированных массивах."
            ),
            QuizQuestionEntity(
                algorithmId = 2,
                question = "Какая сложность у бинарного поиска?",
                option1 = "O(n)",
                option2 = "O(n²)",
                option3 = "O(log n)",
                option4 = "O(1)",
                correctOption = 3,
                explanation = "На каждом шаге область поиска уменьшается вдвое, поэтому сложность O(log n)."
            ),
            QuizQuestionEntity(
                algorithmId = 2,
                question = "Что происходит, когда искомый элемент меньше среднего?",
                option1 = "Ищем в левой половине",
                option2 = "Ищем в правой половине",
                option3 = "Завершаем поиск",
                option4 = "Меняем элементы местами",
                correctOption = 1,
                explanation = "Если элемент меньше среднего, он находится в левой половине массива."
            ),

            // ===== Quick Sort (id=3) =====
            QuizQuestionEntity(
                algorithmId = 3,
                question = "Какая стратегия используется в Quick Sort?",
                option1 = "Динамическое программирование",
                option2 = "Жадные алгоритмы",
                option3 = "Разделяй и властвуй",
                option4 = "Поиск с возвратом",
                correctOption = 3,
                explanation = "Quick Sort использует стратегию 'разделяй и властвуй'."
            ),
            QuizQuestionEntity(
                algorithmId = 3,
                question = "Что такое опорный элемент (pivot) в Quick Sort?",
                option1 = "Первый элемент массива",
                option2 = "Элемент, вокруг которого происходит разделение",
                option3 = "Самый большой элемент",
                option4 = "Самый маленький элемент",
                correctOption = 2,
                explanation = "Опорный элемент используется для разделения массива на две части."
            ),
            QuizQuestionEntity(
                algorithmId = 3,
                question = "Какая сложность Quick Sort в лучшем случае?",
                option1 = "O(n)",
                option2 = "O(n log n)",
                option3 = "O(n²)",
                option4 = "O(log n)",
                correctOption = 2,
                explanation = "В лучшем случае массив делится пополам, давая O(n log n)."
            ),

            // ===== DFS (id=4) =====
            QuizQuestionEntity(
                algorithmId = 4,
                question = "Какую структуру данных использует DFS?",
                option1 = "Очередь",
                option2 = "Стек",
                option3 = "Массив",
                option4 = "Список",
                correctOption = 2,
                explanation = "DFS использует стек (явный или неявный через рекурсию)."
            ),
            QuizQuestionEntity(
                algorithmId = 4,
                question = "Что означает DFS?",
                option1 = "Breadth-First Search",
                option2 = "Depth-First Search",
                option3 = "Best-First Search",
                option4 = "Binary Search",
                correctOption = 2,
                explanation = "DFS — поиск в глубину (Depth-First Search)."
            ),
            QuizQuestionEntity(
                algorithmId = 4,
                question = "Какой обход использует DFS?",
                option1 = "По уровням",
                option2 = "Вглубь",
                option3 = "В ширину",
                option4 = "Случайный",
                correctOption = 2,
                explanation = "DFS идёт вглубь графа, пока это возможно."
            ),

            // ===== BFS (id=5) =====
            QuizQuestionEntity(
                algorithmId = 5,
                question = "Какую структуру данных использует BFS?",
                option1 = "Очередь",
                option2 = "Стек",
                option3 = "Массив",
                option4 = "Дерево",
                correctOption = 1,
                explanation = "BFS использует очередь для обхода по уровням."
            ),
            QuizQuestionEntity(
                algorithmId = 5,
                question = "Что означает BFS?",
                option1 = "Breadth-First Search",
                option2 = "Depth-First Search",
                option3 = "Binary Search",
                option4 = "Best-First Search",
                correctOption = 1,
                explanation = "BFS — поиск в ширину (Breadth-First Search)."
            ),
            QuizQuestionEntity(
                algorithmId = 5,
                question = "Для чего лучше всего подходит BFS?",
                option1 = "Поиск кратчайшего пути в невзвешенном графе",
                option2 = "Поиск в глубину",
                option3 = "Сортировка массива",
                option4 = "Сжатие данных",
                correctOption = 1,
                explanation = "BFS находит кратчайший путь в невзвешенном графе."
            ),

            // ===== Merge Sort (id=6) =====
            QuizQuestionEntity(
                algorithmId = 6,
                question = "Какая стратегия используется в Merge Sort?",
                option1 = "Динамическое программирование",
                option2 = "Жадные алгоритмы",
                option3 = "Разделяй и властвуй",
                option4 = "Поиск с возвратом",
                correctOption = 3,
                explanation = "Merge Sort использует стратегию 'разделяй и властвуй'."
            ),
            QuizQuestionEntity(
                algorithmId = 6,
                question = "Какая дополнительная память требуется для Merge Sort?",
                option1 = "O(1)",
                option2 = "O(log n)",
                option3 = "O(n)",
                option4 = "O(n²)",
                correctOption = 3,
                explanation = "Merge Sort требует O(n) дополнительной памяти для слияния."
            ),
            QuizQuestionEntity(
                algorithmId = 6,
                question = "Что происходит на этапе 'слияния' в Merge Sort?",
                option1 = "Массив делится пополам",
                option2 = "Две отсортированные половины объединяются",
                option3 = "Выбирается опорный элемент",
                option4 = "Элементы сравниваются попарно",
                correctOption = 2,
                explanation = "На этапе слияния объединяются две отсортированные половины."
            ),

            // ===== Dijkstra (id=7) =====
            QuizQuestionEntity(
                algorithmId = 7,
                question = "Для чего используется алгоритм Дейкстры?",
                option1 = "Сортировка массива",
                option2 = "Поиск кратчайшего пути в графе",
                option3 = "Поиск элемента в массиве",
                option4 = "Обход дерева",
                correctOption = 2,
                explanation = "Алгоритм Дейкстры находит кратчайшие пути от одной вершины до всех остальных."
            ),
            QuizQuestionEntity(
                algorithmId = 7,
                question = "Какое важное ограничение у алгоритма Дейкстры?",
                option1 = "Граф должен быть полным",
                option2 = "Все рёбра должны быть неотрицательными",
                option3 = "Граф должен быть ориентированным",
                option4 = "Граф должен быть неориентированным",
                correctOption = 2,
                explanation = "Алгоритм Дейкстры работает только с неотрицательными весами рёбер."
            ),
            QuizQuestionEntity(
                algorithmId = 7,
                question = "Какой структурой данных обычно оптимизируют алгоритм Дейкстры?",
                option1 = "Массив",
                option2 = "Стек",
                option3 = "Очередь с приоритетом",
                option4 = "Связный список",
                correctOption = 3,
                explanation = "Очередь с приоритетом (куча) позволяет быстрее находить вершину с минимальным расстоянием."
            )
        )

        dao.insertQuestions(allQuestions)

        // ============= ДОСТИЖЕНИЯ =============
        val achievements = listOf(
            AchievementEntity(
                achievementId = "first_view",
                title = "🌱 Новичок",
                description = "Просмотрел первый алгоритм",
                icon = "🌱",
                condition = "Просмотреть любой алгоритм",
                isUnlocked = false,
                unlockedDate = 0
            ),
            AchievementEntity(
                achievementId = "three_views",
                title = "📚 Студент",
                description = "Просмотрел 3 алгоритма",
                icon = "📚",
                condition = "Просмотреть 3 разных алгоритма",
                isUnlocked = false,
                unlockedDate = 0
            ),
            AchievementEntity(
                achievementId = "five_views",
                title = "🧠 Знаток",
                description = "Просмотрел 5 алгоритмов",
                icon = "🧠",
                condition = "Просмотреть 5 разных алгоритмов",
                isUnlocked = false,
                unlockedDate = 0
            ),
            AchievementEntity(
                achievementId = "all_views",
                title = "⭐ Мастер",
                description = "Просмотрел все алгоритмы",
                icon = "⭐",
                condition = "Просмотреть все 7 алгоритмов",
                isUnlocked = false,
                unlockedDate = 0
            ),
            AchievementEntity(
                achievementId = "first_quiz",
                title = "🎯 Первый тест",
                description = "Правильно ответил на первый тест",
                icon = "🎯",
                condition = "Сдать тест по любому алгоритму",
                isUnlocked = false,
                unlockedDate = 0
            ),
            AchievementEntity(
                achievementId = "perfect_score",
                title = "💯 Отличник",
                description = "Правильно ответил на все вопросы в тесте",
                icon = "💯",
                condition = "Набрать 100% в тесте",
                isUnlocked = false,
                unlockedDate = 0
            ),
            AchievementEntity(
                achievementId = "three_perfect",
                title = "🏆 Профессор",
                description = "Сдал 3 теста на 100%",
                icon = "🏆",
                condition = "Сдать 3 теста с максимальным баллом",
                isUnlocked = false,
                unlockedDate = 0
            ),
            AchievementEntity(
                achievementId = "all_quizzes",
                title = "🎓 Выпускник",
                description = "Сдал все тесты",
                icon = "🎓",
                condition = "Сдать тесты по всем алгоритмам",
                isUnlocked = false,
                unlockedDate = 0
            )
        )

        dao.insertAchievements(achievements)
    }
}
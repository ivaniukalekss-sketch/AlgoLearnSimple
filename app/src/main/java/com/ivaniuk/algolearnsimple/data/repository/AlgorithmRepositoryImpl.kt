package com.ivaniuk.algolearnsimple.data.repository

import com.ivaniuk.algolearnsimple.data.local.LocalStorage
import com.ivaniuk.algolearnsimple.domain.model.Algorithm
import com.ivaniuk.algolearnsimple.domain.model.AlgorithmCategory
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class AlgorithmRepositoryImpl(private val localStorage: LocalStorage) : AlgorithmRepository {

    private val favoriteIds = localStorage.getFavorites().toMutableSet()

    private val initialAlgorithms = listOf(
        Algorithm(
            id = 1,
            title = "Bubble Sort",
            description = "Простейший алгоритм сортировки, который многократно проходит по списку, сравнивая соседние элементы и меняя их местами, если они находятся в неправильном порядке.",
            category = AlgorithmCategory.SORTING,
            complexity = "O(n²)",
            codeExample = """
                fun bubbleSort(arr: IntArray) {
                    val n = arr.size
                    for (i in 0 until n - 1) {
                        for (j in 0 until n - i - 1) {
                            if (arr[j] > arr[j + 1]) {
                                val temp = arr[j]
                                arr[j] = arr[j + 1]
                                arr[j + 1] = temp
                            }
                        }
                    }
                }
            """.trimIndent(),
            steps = listOf(
                "Начинаем с первого элемента",
                "Сравниваем текущий элемент со следующим",
                "Если текущий больше следующего - меняем местами",
                "Переходим к следующей паре",
                "Повторяем до конца массива"
            ),
            isFavorite = favoriteIds.contains(1)
        ),
        Algorithm(
            id = 2,
            title = "Binary Search",
            description = "Эффективный алгоритм поиска элемента в отсортированном массиве. Работает по принципу 'разделяй и властвуй'.",
            category = AlgorithmCategory.SEARCHING,
            complexity = "O(log n)",
            codeExample = """
                fun binarySearch(arr: IntArray, target: Int): Int {
                    var left = 0
                    var right = arr.size - 1
                    
                    while (left <= right) {
                        val mid = left + (right - left) / 2
                        
                        when {
                            arr[mid] == target -> return mid
                            arr[mid] < target -> left = mid + 1
                            else -> right = mid - 1
                        }
                    }
                    return -1
                }
            """.trimIndent(),
            steps = listOf(
                "Проверяем середину массива",
                "Если элемент найден - возвращаем индекс",
                "Если искомый элемент меньше среднего - ищем в левой половине",
                "Если больше - ищем в правой половине",
                "Повторяем пока не найдём элемент"
            ),
            isFavorite = favoriteIds.contains(2)
        ),
        Algorithm(
            id = 3,
            title = "Quick Sort",
            description = "Эффективный алгоритм сортировки, использующий стратегию 'разделяй и властвуй'. Выбирается опорный элемент, и массив разделяется на две части.",
            category = AlgorithmCategory.SORTING,
            complexity = "O(n log n)",
            codeExample = """
                fun quickSort(arr: IntArray, low: Int = 0, high: Int = arr.size - 1) {
                    if (low < high) {
                        val pi = partition(arr, low, high)
                        quickSort(arr, low, pi - 1)
                        quickSort(arr, pi + 1, high)
                    }
                }
            """.trimIndent(),
            steps = listOf(
                "Выбираем опорный элемент",
                "Разделяем массив на две части",
                "В левой - элементы меньше опорного",
                "В правой - элементы больше опорного",
                "Рекурсивно сортируем обе части"
            ),
            isFavorite = favoriteIds.contains(3)
        ),
        Algorithm(
            id = 4,
            title = "DFS (Depth-First Search)",
            description = "Алгоритм обхода или поиска в структурах данных типа дерева или графа. Исследует ветви графа настолько глубоко, насколько это возможно.",
            category = AlgorithmCategory.GRAPH,
            complexity = "O(V + E)",
            codeExample = """
                fun dfs(graph: Map<Int, List<Int>>, start: Int) {
                    val visited = mutableSetOf<Int>()
                    val stack = ArrayDeque<Int>()
                    
                    stack.addLast(start)
                    
                    while (stack.isNotEmpty()) {
                        val vertex = stack.removeLast()
                        if (vertex !in visited) {
                            visited.add(vertex)
                            
                            graph[vertex]?.forEach { neighbor ->
                                if (neighbor !in visited) {
                                    stack.addLast(neighbor)
                                }
                            }
                        }
                    }
                }
            """.trimIndent(),
            steps = listOf(
                "Начинаем с начальной вершины",
                "Помечаем вершину как посещённую",
                "Для каждой соседней вершины",
                "Если не посещена - рекурсивно вызываем DFS"
            ),
            isFavorite = favoriteIds.contains(4)
        ),
        Algorithm(
            id = 5,
            title = "BFS (Breadth-First Search)",
            description = "Алгоритм обхода графа по уровням. Исследует все соседние вершины на текущем уровне, прежде чем перейти на следующий уровень. Используется для поиска кратчайшего пути в невзвешенных графах.",
            category = AlgorithmCategory.GRAPH,
            complexity = "O(V + E)",
            codeExample = """
                fun bfs(graph: Map<Int, List<Int>>, start: Int): List<Int> {
                    val visited = mutableSetOf<Int>()
                    val queue = ArrayDeque<Int>()
                    val result = mutableListOf<Int>()
                    
                    queue.addLast(start)
                    visited.add(start)
                    
                    while (queue.isNotEmpty()) {
                        val current = queue.removeFirst()
                        result.add(current)
                        
                        graph[current]?.forEach { neighbor ->
                            if (neighbor !in visited) {
                                visited.add(neighbor)
                                queue.addLast(neighbor)
                            }
                        }
                    }
                    return result
                }
            """.trimIndent(),
            steps = listOf(
                "Добавляем стартовую вершину в очередь и помечаем как посещённую",
                "Пока очередь не пуста, извлекаем первую вершину",
                "Обрабатываем текущую вершину",
                "Для каждого соседа, который ещё не посещён",
                "Добавляем соседа в очередь и помечаем как посещённого",
                "Повторяем пока очередь не опустеет"
            ),
            isFavorite = favoriteIds.contains(5)
        ),
        Algorithm(
            id = 6,
            title = "Merge Sort",
            description = "Эффективный алгоритм сортировки, основанный на принципе 'разделяй и властвуй'. Разбивает массив на две половины, рекурсивно сортирует их, а затем сливает в отсортированный массив.",
            category = AlgorithmCategory.SORTING,
            complexity = "O(n log n)",
            codeExample = """
                fun mergeSort(arr: IntArray, left: Int, right: Int) {
                    if (left < right) {
                        val mid = left + (right - left) / 2
                        mergeSort(arr, left, mid)
                        mergeSort(arr, mid + 1, right)
                        merge(arr, left, mid, right)
                    }
                }
                
                fun merge(arr: IntArray, left: Int, mid: Int, right: Int) {
                    val leftArray = arr.sliceArray(left..mid)
                    val rightArray = arr.sliceArray(mid + 1..right)
                    
                    var i = 0
                    var j = 0
                    var k = left
                    
                    while (i < leftArray.size && j < rightArray.size) {
                        if (leftArray[i] <= rightArray[j]) {
                            arr[k++] = leftArray[i++]
                        } else {
                            arr[k++] = rightArray[j++]
                        }
                    }
                    
                    while (i < leftArray.size) arr[k++] = leftArray[i++]
                    while (j < rightArray.size) arr[k++] = rightArray[j++]
                }
            """.trimIndent(),
            steps = listOf(
                "Если массив имеет размер 1 или пуст — он уже отсортирован (базовый случай)",
                "Находим середину массива и делим его на две половины",
                "Рекурсивно применяем mergeSort к левой половине",
                "Рекурсивно применяем mergeSort к правой половине",
                "Сливаем две отсортированные половины в один отсортированный массив",
                "Повторяем пока весь массив не будет отсортирован"
            ),
            isFavorite = favoriteIds.contains(6)
        ),
        Algorithm(
            id = 7,
            title = "Dijkstra",
            description = "Алгоритм находит кратчайшие пути от одной вершины до всех остальных во взвешенном графе с неотрицательными весами. Широко используется в навигационных системах и сетевой маршрутизации.",
            category = AlgorithmCategory.GRAPH,
            complexity = "O((V + E) log V)",
            codeExample = """
        fun dijkstra(graph: Map<Int, List<Pair<Int, Int>>>, start: Int): Map<Int, Int> {
            val distances = mutableMapOf<Int, Int>().apply {
                graph.keys.forEach { this[it] = Int.MAX_VALUE }
                this[start] = 0
            }
            val visited = mutableSetOf<Int>()
            val queue = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })
            queue.add(start to 0)
            
            while (queue.isNotEmpty()) {
                val (current, dist) = queue.poll()
                if (current in visited) continue
                visited.add(current)
                
                graph[current]?.forEach { (neighbor, weight) ->
                    val newDist = dist + weight
                    if (newDist < distances[neighbor]!!) {
                        distances[neighbor] = newDist
                        queue.add(neighbor to newDist)
                    }
                }
            }
            return distances
        }
    """.trimIndent(),
            steps = listOf(
                "Устанавливаем расстояние до стартовой вершины = 0, до остальных = ∞",
                "Помещаем стартовую вершину в очередь с приоритетом",
                "Пока очередь не пуста, извлекаем вершину с минимальным расстоянием",
                "Для каждого соседа вычисляем новое расстояние через текущую вершину",
                "Если новое расстояние меньше сохранённого — обновляем и добавляем в очередь",
                "Повторяем, пока все вершины не будут обработаны"
            ),
            isFavorite = favoriteIds.contains(7)
        ),
        Algorithm(
            id = 8,
            title = "Selection Sort",
            description = "Алгоритм сортировки выбором. На каждом шаге находит минимальный элемент в неотсортированной части и ставит его на своё место.",
            category = AlgorithmCategory.SORTING,
            complexity = "O(n²)",
            codeExample = """
        fun selectionSort(arr: IntArray) {
            for (i in arr.indices) {
                var minIndex = i
                for (j in i + 1 until arr.size) {
                    if (arr[j] < arr[minIndex]) {
                        minIndex = j
                    }
                }
                val temp = arr[i]
                arr[i] = arr[minIndex]
                arr[minIndex] = temp
            }
        }
    """.trimIndent(),
            steps = listOf(
                "Находим минимальный элемент в массиве",
                "Меняем его местами с первым элементом",
                "Повторяем для оставшейся части массива",
                "Каждый шаг уменьшает неотсортированную часть"
            ),
            isFavorite = favoriteIds.contains(8)
        ),
        Algorithm(
            id = 9,
            title = "Insertion Sort",
            description = "Алгоритм сортировки вставками. Проходит по массиву и вставляет каждый элемент в уже отсортированную часть.",
            category = AlgorithmCategory.SORTING,
            complexity = "O(n²)",
            codeExample = """
        fun insertionSort(arr: IntArray) {
            for (i in 1 until arr.size) {
                val key = arr[i]
                var j = i - 1
                while (j >= 0 && arr[j] > key) {
                    arr[j + 1] = arr[j]
                    j--
                }
                arr[j + 1] = key
            }
        }
    """.trimIndent(),
            steps = listOf(
                "Берём следующий элемент",
                "Сдвигаем большие элементы вправо",
                "Вставляем элемент на правильную позицию",
                "Повторяем для всех элементов"
            ),
            isFavorite = favoriteIds.contains(9)
        ),
        Algorithm(
            id = 10,
            title = "Linear Search",
            description = "Простейший алгоритм поиска. Последовательно проверяет каждый элемент массива до нахождения нужного.",
            category = AlgorithmCategory.SEARCHING,
            complexity = "O(n)",
            codeExample = """
        fun linearSearch(arr: IntArray, target: Int): Int {
            for (i in arr.indices) {
                if (arr[i] == target) {
                    return i
                }
            }
            return -1
        }
    """.trimIndent(),
            steps = listOf(
                "Начинаем с первого элемента",
                "Сравниваем текущий элемент с искомым",
                "Если совпадает — возвращаем индекс",
                "Если нет — переходим к следующему",
                "Если дошли до конца — элемент не найден"
            ),
            isFavorite = favoriteIds.contains(10)
        )
    )

    private val _algorithms = MutableStateFlow(initialAlgorithms)

    override fun getAllAlgorithms(): Flow<List<Algorithm>> = _algorithms

    override fun getAlgorithmsByCategory(category: String): Flow<List<Algorithm>> =
        _algorithms.map { algorithms ->
            algorithms.filter { it.category.name == category }
        }

    override fun getFavoriteAlgorithms(): Flow<List<Algorithm>> =
        _algorithms.map { algorithms ->
            algorithms.filter { it.isFavorite }
        }

    override suspend fun toggleFavorite(algorithmId: Int) {
        delay(50)

        _algorithms.update { currentAlgorithms ->
            currentAlgorithms.map { algorithm ->
                if (algorithm.id == algorithmId) {
                    val newFavoriteState = !algorithm.isFavorite

                    if (newFavoriteState) {
                        favoriteIds.add(algorithmId)
                    } else {
                        favoriteIds.remove(algorithmId)
                    }

                    localStorage.saveFavorites(favoriteIds)
                    algorithm.copy(isFavorite = newFavoriteState)
                } else {
                    algorithm
                }
            }
        }
    }

    override suspend fun getAlgorithmById(id: Int): Algorithm? {
        delay(30)
        val algorithm = _algorithms.value.find { it.id == id }

        algorithm?.let {
            localStorage.addToHistory(it.id, it.title)
        }

        return algorithm
    }

    override suspend fun getViewedCount(): Int {
        return localStorage.getHistoryCount()
    }
}
AlgoLearnSimple
Мобильное приложение для изучения алгоритмов с визуализацией. Разработано в рамках курсового проекта.

Возможности:
- Визуализация 5 алгоритмов (Bubble Sort, Quick Sort, Binary Search, DFS, BFS)
- Пошаговая анимация с управлением скоростью
- Сохранение избранных алгоритмов
- Работа офлайн
- Темная/светлая тема

Архитектура:
- Clean Architecture (Data-Domain-Presentation)
- MVVM с Jetpack Compose
- Kotlin Coroutines & Flow

Структура проекта:
app/src/main/java/com/ivanluk/algolearnsimple/
├── MainActivity.kt
├── AppContainer.kt
├── core/theme/          # Дизайн-система
├── data/               # Data слой (репозитории, хранилище)
├── domain/             # Domain слой (модели, визуализаторы)
└── presentation/       # UI (экран, компоненты, ViewModel)

Технологии:
- Kotlin
- Jetpack Compose
- ViewModel + Navigation Compose
- SharedPreferences (локальное хранение)
- Gradle Kotlin DSL

Тестирование:
- Модульные тесты для визуализаторов
- Инструментальные тесты для репозитория
- Покрытие кода: ~65%

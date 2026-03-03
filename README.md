# AlgoLearnSimple

Мобильное приложение для интерактивного изучения алгоритмов с пошаговой визуализацией. 
Разработано для Android (min API 26).

## Функциональность
*   Пошаговая визуализация алгоритмов (Bubble Sort, Quick Sort, Binary Search, DFS, BFS).
*   Управление скоростью анимации.
*   Генерация случайных входных данных.
*   Добавление алгоритмов в "Избранное".
*   Полностью оффлайн-работа.

## Технологии
*   Язык: Kotlin
*   UI: Jetpack Compose (Material Design 3)
*   Архитектура: Clean Architecture + MVVM
*   Асинхронность: Kotlin Coroutines & Flow
*   Навигация: Jetpack Navigation Compose

## Сборка и установка

### Требования
*   Android Studio Hedgehog (2023.1.1) или новее
*   JDK 11
*   Android SDK (min API 26)

### Инструкция по сборке
1.  Клонируйте репозиторий: `git clone https://github.com/ivaniukalekss-sketch/AlgoLearnSimple.git`
2.  Откройте проект в Android Studio.
3.  Дождитесь синхронизации Gradle и загрузки зависимостей.
4.  Для сборки APK выполните: `Build -> Build Bundle(s) / APK(s) -> Build APK(s)`.
5.  Готовый APK будет находится в папке `app/build/outputs/apk/debug/`.

### Установка на устройство
1.  Включите "Отладку по USB" на вашем Android-устройстве.
2.  Подключите устройство к компьютеру.
3.  В Android Studio выберите ваше устройство и нажмите кнопку `Run`.
4.  Или скопируйте собранный APK-файл на устройство и откройте его для установки.
- domain/  (Domain слой (модели, визуализаторы))
- presentation/ (UI (экран, компоненты, ViewModel))

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

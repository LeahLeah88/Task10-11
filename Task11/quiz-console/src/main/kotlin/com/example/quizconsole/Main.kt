package com.example.quizconsole

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

data class Choice(
    val id: Int,
    val text: String,
    val points: Int
)

data class Selection(
    val questionNumber: Int,
    val choiceText: String,
    val points: Int
)

data class FinalResult(
    val title: String,
    val description: String
)

class QuizState {
    var totalPoints: Int = 0
        private set

    val selections: MutableList<Selection> = mutableListOf()

    fun addSelection(questionNumber: Int, choice: Choice) {
        totalPoints += choice.points
        selections += Selection(
            questionNumber = questionNumber,
            choiceText = choice.text,
            points = choice.points
        )
    }
}

fun calculateFinal(totalPoints: Int): FinalResult {
    return when (totalPoints) {
        in 0..3 -> FinalResult(
            title = "Спокойный режим",
            description = "Вы предпочитаете размеренный темп и взвешенные решения."
        )
        in 4..5 -> FinalResult(
            title = "Сбалансированная стратегия",
            description = "Вы сочетаете планирование и гибкость — это помогает двигаться эффективно."
        )
        else -> FinalResult(
            title = "Энергичный лидер",
            description = "Вы быстро действуете и умеете брать инициативу в нужный момент."
        )
    }
}

private fun readChoice(questionNumber: Int, questionText: String, choices: List<Choice>): Choice {
    println("Вопрос $questionNumber: $questionText")
    choices.forEach { c ->
        println("${c.id}. ${c.text} (+${c.points})")
    }

    while (true) {
        print("Введите номер ответа: ")
        val raw = readLine()?.trim()
        val selectedId = raw?.toIntOrNull()
        if (selectedId != null) {
            val choice = choices.firstOrNull { it.id == selectedId }
            if (choice != null) return choice
        }
        println("Некорректный ввод. Попробуйте ещё раз.\n")
    }
}

fun main() = runBlocking {
    // Состояние хранится только в памяти процесса: при повторном запуске всё обнулится автоматически.
    val state = QuizState()

    println("=== Экран 1 ===")
    val q1Choices = listOf(
        Choice(id = 1, text = "Предпочитаю планировать заранее", points = 0),
        Choice(id = 2, text = "Нравится сочетать план и импровизацию", points = 2),
        Choice(id = 3, text = "Люблю действовать быстро", points = 4)
    )
    val choice1 = readChoice(
        questionNumber = 1,
        questionText = "Какой стиль работы вам ближе?",
        choices = q1Choices
    )
    state.addSelection(questionNumber = 1, choice = choice1)

    delay(300)
    println()

    println("=== Экран 2 ===")
    val q2Choices = listOf(
        Choice(id = 1, text = "Слушать и дополнять", points = 0),
        Choice(id = 2, text = "Предлагать идеи постепенно", points = 2),
        Choice(id = 3, text = "Брать инициативу и предлагать решения", points = 4)
    )
    val choice2 = readChoice(
        questionNumber = 2,
        questionText = "Что больше подходит вам в командной работе?",
        choices = q2Choices
    )
    state.addSelection(questionNumber = 2, choice = choice2)

    delay(300)
    println()

    println("=== Экран 3: Результат ===")
    println("Баллы: ${state.totalPoints}\n")

    state.selections
        .sortedBy { it.questionNumber }
        .forEach { sel ->
            println("Вопрос ${sel.questionNumber}: ${sel.choiceText} (+${sel.points})")
        }
    println()

    val final = calculateFinal(state.totalPoints)
    println("Финал: ${final.title}")
    println(final.description)
}


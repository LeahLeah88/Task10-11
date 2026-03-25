package com.example.quizconsole

import kotlin.test.Test
import kotlin.test.assertEquals

class QuizFinalTest {

    @Test
    fun `calculateFinal returns спокойный режим for 0 points`() {
        val result = calculateFinal(0)
        assertEquals("Спокойный режим", result.title)
    }

    @Test
    fun `calculateFinal returns спокойный режим for 3 points`() {
        val result = calculateFinal(3)
        assertEquals("Спокойный режим", result.title)
    }

    @Test
    fun `calculateFinal returns сбалансированная стратегия for 4 points`() {
        val result = calculateFinal(4)
        assertEquals("Сбалансированная стратегия", result.title)
    }

    @Test
    fun `calculateFinal returns сбалансированная стратегия for 5 points`() {
        val result = calculateFinal(5)
        assertEquals("Сбалансированная стратегия", result.title)
    }

    @Test
    fun `calculateFinal returns энергичный лидер for 6 points`() {
        val result = calculateFinal(6)
        assertEquals("Энергичный лидер", result.title)
    }

    @Test
    fun `calculateFinal returns энергичный лидер for 8 points`() {
        val result = calculateFinal(8)
        assertEquals("Энергичный лидер", result.title)
    }
}


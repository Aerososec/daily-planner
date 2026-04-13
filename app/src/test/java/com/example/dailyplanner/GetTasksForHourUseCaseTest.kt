package com.example.dailyplanner

import com.example.dailyplanner.domain.model.Task
import com.example.dailyplanner.domain.usecase.GetTasksForHourUseCase
import com.example.dailyplanner.presentaion.utils.toStartOfDay
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.LocalDate

class GetTasksForHourUseCaseTest {

    private val useCase = GetTasksForHourUseCase()

    @Test
    fun `task inside one hour should appear in correct slot`() {
        val startDay = LocalDate.of(2024, 1, 1).toStartOfDay()

        val task = Task(
            id = 1,
            dateStart = startDay + 10 * 60 * 60 * 1000,
            dateFinish = startDay + 11 * 60 * 60 * 1000,
            name = "Task",
            description = ""
        )

        val result = useCase(listOf(task), startDay)

        val hour10 = result[10]

        assertEquals(1, hour10.tasks.size)
        assertEquals("Task", hour10.tasks.first().name)
    }

    @Test
    fun `task overlapping two hours should appear in both`() {
        val startDay = LocalDate.of(2024, 1, 1).toStartOfDay()

        val task = Task(
            id = 1,
            dateStart = startDay + 10 * 60 * 60 * 1000 + 30 * 60 * 1000, // 10:30
            dateFinish = startDay + 11 * 60 * 60 * 1000 + 30 * 60 * 1000, // 11:30
            name = "Overlap",
            description = ""
        )

        val result = useCase(listOf(task), startDay)

        val hour10 = result[10]
        val hour11 = result[11]

        assertTrue(hour10.tasks.isNotEmpty())
        assertTrue(hour11.tasks.isNotEmpty())
    }
}
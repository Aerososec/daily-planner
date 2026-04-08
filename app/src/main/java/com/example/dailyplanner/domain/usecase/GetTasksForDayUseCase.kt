package com.example.dailyplanner.domain.usecase

import com.example.dailyplanner.domain.model.Task
import com.example.dailyplanner.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksForDayUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(startDate : Long, finishDate : Long) : List<Task>{
        return taskRepository.getTasksForDay(startDate, finishDate)
    }
}
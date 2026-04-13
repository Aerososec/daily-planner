package com.example.dailyplanner.domain.usecase

import com.example.dailyplanner.domain.model.Task
import com.example.dailyplanner.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task){
        taskRepository.deleteTsk(task)
    }
}
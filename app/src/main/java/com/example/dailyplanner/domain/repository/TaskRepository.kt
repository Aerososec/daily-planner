package com.example.dailyplanner.domain.repository

import com.example.dailyplanner.domain.model.Task

interface TaskRepository {
    suspend fun getTasksForDay(startDate : Long, finishDate : Long) : List<Task>
    suspend fun insertTask(task: Task)
    suspend fun deleteTsk(task: Task)
}
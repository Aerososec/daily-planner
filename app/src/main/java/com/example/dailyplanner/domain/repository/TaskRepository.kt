package com.example.dailyplanner.domain.repository

import com.example.dailyplanner.domain.model.Task

interface TaskRepository {
    suspend fun getTasksForDay() : List<Task>
    suspend fun insertTask()
}
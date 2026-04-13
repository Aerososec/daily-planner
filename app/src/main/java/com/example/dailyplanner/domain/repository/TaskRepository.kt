package com.example.dailyplanner.domain.repository

import com.example.dailyplanner.data.model.TaskDbModel
import com.example.dailyplanner.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasksForDay(startDate : Long, finishDate : Long) : Flow<List<Task>>
    suspend fun insertTask(task: Task)
    suspend fun deleteTsk(task: Task)
}
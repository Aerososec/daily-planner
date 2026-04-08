package com.example.dailyplanner.data.repository

import com.example.dailyplanner.data.db.TaskDao
import com.example.dailyplanner.data.model.mapper.TaskMapper
import com.example.dailyplanner.domain.model.Task
import com.example.dailyplanner.domain.repository.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao, private val mapper: TaskMapper) : TaskRepository {
    override suspend fun getTasksForDay(
        startDate: Long,
        finishDate: Long
    ): List<Task> {
        return mapper.dbModelToEntityList(taskDao.getTasksForDay(startDate, finishDate))
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(mapper.entityToDbModel(task))
    }
}
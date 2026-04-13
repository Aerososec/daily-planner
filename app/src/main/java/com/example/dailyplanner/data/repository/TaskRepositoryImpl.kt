package com.example.dailyplanner.data.repository

import com.example.dailyplanner.data.db.TaskDao
import com.example.dailyplanner.data.model.TaskDbModel
import com.example.dailyplanner.data.model.mapper.TaskMapper
import com.example.dailyplanner.domain.model.Task
import com.example.dailyplanner.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao, private val mapper: TaskMapper) : TaskRepository {
    override fun getTasksForDay(
        startDate: Long,
        finishDate: Long
    ): Flow<List<Task>> {
        return taskDao.getTasksForDay(startDate, finishDate)
            .map { mapper.dbModelToEntityList(it) }
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(mapper.entityToDbModel(task))
    }

    override suspend fun deleteTsk(task: Task) {
        taskDao.deleteTask(mapper.entityToDbModel(task))
    }
}
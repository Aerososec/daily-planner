package com.example.dailyplanner.data.model.mapper

import com.example.dailyplanner.data.model.TaskDbModel
import com.example.dailyplanner.domain.model.Task
import javax.inject.Inject

class TaskMapper @Inject constructor() {
    fun entityToDbModel(task: Task): TaskDbModel {
        return TaskDbModel(
            dateStart = task.dateStart,
            dateFinish = task.dateFinish,
            name = task.name,
            description = task.description
        )
    }

    fun dbModelToEntity(task: TaskDbModel): Task{
        return Task(
            id = task.id,
            dateStart = task.dateStart,
            dateFinish = task.dateFinish,
            name = task.name,
            description = task.description
        )
    }

    fun dbModelToEntityList(list : List<TaskDbModel>) : List<Task>{
        return list.map { dbModelToEntity(it) }
    }
}
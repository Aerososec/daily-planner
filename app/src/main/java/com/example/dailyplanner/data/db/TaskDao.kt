package com.example.dailyplanner.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dailyplanner.data.model.TaskDbModel


@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE dateStart < :finishDate AND dateFinish > :startDate")
    suspend fun getTasksForDay(startDate : Long, finishDate : Long) : List<TaskDbModel>

    @Insert
    suspend fun insertTask(task: TaskDbModel)
}
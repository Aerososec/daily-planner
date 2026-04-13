package com.example.dailyplanner.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dailyplanner.data.model.TaskDbModel
import com.example.dailyplanner.domain.model.Task
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE dateStart < :finishDate AND dateFinish > :startDate")
    fun getTasksForDay(startDate : Long, finishDate : Long) : Flow<List<TaskDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskDbModel)

    @Delete
    suspend fun deleteTask(task: TaskDbModel)
}
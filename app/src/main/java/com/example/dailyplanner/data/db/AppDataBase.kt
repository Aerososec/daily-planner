package com.example.dailyplanner.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dailyplanner.data.model.TaskDbModel

@Database(entities = [TaskDbModel::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun taskDao() : TaskDao
}
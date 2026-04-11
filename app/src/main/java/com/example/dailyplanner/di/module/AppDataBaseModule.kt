package com.example.dailyplanner.di.module

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dailyplanner.data.db.AppDataBase
import com.example.dailyplanner.data.db.TaskDao
import dagger.Module
import dagger.Provides


@Module
class AppDataBaseModule {
    @Provides
    fun provideDataBase(application: Application) : AppDataBase{

        application.deleteDatabase(DB_NAME)

        return Room.databaseBuilder(
            application,
            AppDataBase::class.java,
            DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideTaskDao(db : AppDataBase) : TaskDao{
        return db.taskDao()
    }


    companion object{
        private const val DB_NAME = "DAILY_PLANNER_DATABASE"
    }
}
package com.example.dailyplanner.di.module

import com.example.dailyplanner.data.repository.TaskRepositoryImpl
import com.example.dailyplanner.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module

@Module
interface TaskRepositoryModule {
    @Binds
    fun bindTaskRepository(impl : TaskRepositoryImpl) : TaskRepository
}
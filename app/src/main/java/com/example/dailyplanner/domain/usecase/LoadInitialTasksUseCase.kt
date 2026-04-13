package com.example.dailyplanner.domain.usecase

import android.app.Application
import android.content.Context
import com.example.dailyplanner.domain.model.Task
import com.example.dailyplanner.domain.repository.TaskRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject


class LoadInitialTasksUseCase @Inject constructor(
    private val application: Application,
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke(){
        val gson = Gson()
        val jsonString = application.assets.open("initial_tasks.json").bufferedReader().use { it.readText() }

        val type = object : TypeToken<List<Task>>() {}.type
        val tasks: List<Task> = gson.fromJson(jsonString, type)

        tasks.forEach { task ->
            taskRepository.insertTask(task)
        }
    }
}
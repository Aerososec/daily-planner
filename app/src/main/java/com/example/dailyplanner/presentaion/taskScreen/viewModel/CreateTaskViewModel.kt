package com.example.dailyplanner.presentaion.taskScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyplanner.domain.model.Task
import com.example.dailyplanner.domain.usecase.DeleteTaskUseCase
import com.example.dailyplanner.domain.usecase.GetTasksForDayUseCase
import com.example.dailyplanner.domain.usecase.InsertTaskUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateTaskViewModel @Inject constructor(
    private val insertTaskUseCase: InsertTaskUseCase,
    private val getTasksForDayUseCase: GetTasksForDayUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    private var currentStartPeriod: Long? = null
    private var currentEndPeriod: Long? = null

    fun loadTasksForPeriod(startPeriod: Long, endPeriod: Long) {
        currentStartPeriod = startPeriod
        currentEndPeriod = endPeriod

        viewModelScope.launch {
            getTasksForDayUseCase(startPeriod, endPeriod).collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    suspend fun insertTask(
        dateStart: Long,
        dateFinish: Long,
        name: String,
        description: String,
        id: Long = 0
    ) {
        val task = Task(
            id = id,
            dateStart = dateStart,
            dateFinish = dateFinish,
            name = name,
            description = description
        )
        insertTaskUseCase(task)
    }

    suspend fun deleteTask(task: Task) {
        deleteTaskUseCase(task)

        currentStartPeriod?.let { start ->
            currentEndPeriod?.let { end ->
                loadTasksForPeriod(start, end)
            }
        }
    }


}
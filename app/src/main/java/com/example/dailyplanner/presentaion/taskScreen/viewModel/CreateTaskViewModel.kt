package com.example.dailyplanner.presentaion.taskScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyplanner.domain.model.Task
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
    private val getTasksForDayUseCase: GetTasksForDayUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    suspend fun insertTask(dateStart : Long, dateFinish : Long, name : String, description : String, id : Long = 0){
        val task = Task(
            id = id,
            dateStart = dateStart,
            dateFinish = dateFinish,
            name = name,
            description = description
        )
        insertTaskUseCase(task)
    }

    suspend fun getTasksForPeriod(startPeriod: Long, endPeriod: Long){
        _tasks.value = getTasksForDayUseCase(startPeriod, endPeriod)
    }
}
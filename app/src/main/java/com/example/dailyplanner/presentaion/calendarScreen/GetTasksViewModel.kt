package com.example.dailyplanner.presentaion.calendarScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyplanner.domain.model.TasksInHour
import com.example.dailyplanner.domain.usecase.GetTasksForDayUseCase
import com.example.dailyplanner.domain.usecase.GetTasksForHourUseCase
import com.example.dailyplanner.domain.usecase.InsertTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetTasksViewModel @Inject constructor(
    private val getTasksForHourUseCase: GetTasksForHourUseCase,
    private val getTasksForDayUseCase: GetTasksForDayUseCase
) : ViewModel() {

    private val _schedule = MutableStateFlow<List<TasksInHour>>(emptyList())
    val schedule = _schedule.asStateFlow()

    fun getTaskForDay(dayStart : Long){
        viewModelScope.launch {
            val dayFinish = dayStart + HOURS_IN_DAY * HOUR_IN_MILLIS
            val dayTasks = getTasksForDayUseCase(dayStart, dayFinish)
            _schedule.value = getTasksForHourUseCase(dayTasks, dayStart)
        }

    }

    companion object{
        private const val HOUR_IN_MILLIS = 60*60*1000
        private const val HOURS_IN_DAY = 24
    }
}
package com.example.dailyplanner.presentaion.calendarScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyplanner.domain.model.TasksInHour
import com.example.dailyplanner.domain.usecase.GetTasksForDayUseCase
import com.example.dailyplanner.domain.usecase.GetTasksForHourUseCase
import com.example.dailyplanner.domain.usecase.InsertTaskUseCase
import com.example.dailyplanner.presentaion.utils.formatStringDate
import com.example.dailyplanner.presentaion.utils.toStartOfDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import javax.inject.Inject

class GetTasksViewModel @Inject constructor(
    private val getTasksForHourUseCase: GetTasksForHourUseCase,
    private val getTasksForDayUseCase: GetTasksForDayUseCase
) : ViewModel() {

    private val _schedule = MutableStateFlow<List<TasksInHour>>(emptyList())
    val schedule = _schedule.asStateFlow()

    fun getTaskForDay(day : LocalDate){
        val selectedDay = day.toStartOfDay()
        viewModelScope.launch {
            val dayFinish = selectedDay + HOURS_IN_DAY * HOUR_IN_MILLIS
            val dayTasks = getTasksForDayUseCase(selectedDay, dayFinish)
            Log.d("TASKS", "----------")
            Log.d("TASKS", dayTasks.toString())
            val value = getTasksForHourUseCase(dayTasks, selectedDay)
            Log.d("TASKS", value.toString())
            Log.d("TASKS", "----------")
            _schedule.value = value
        }
    }

    companion object{
        private const val HOUR_IN_MILLIS = 60*60*1000
        private const val HOURS_IN_DAY = 24
    }
}
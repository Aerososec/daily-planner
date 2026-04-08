package com.example.dailyplanner.domain.usecase

import com.example.dailyplanner.domain.model.Task
import com.example.dailyplanner.domain.model.TasksInHour
import javax.inject.Inject

class GetTasksForHour @Inject constructor() {
    operator fun invoke(listTasks : List<Task>, startDay : Long){

        val tasksByHours = mutableListOf<TasksInHour>()

        for (hour in 0..23){
            val startTime = startDay + hour * HOUR_IN_MILLIS
            val endTime = startTime + HOUR_IN_MILLIS

            val tasksForHour = listTasks.filter { task ->
                task.dateStart < endTime && task.dateFinish > startTime
            }

            tasksByHours.add(
                TasksInHour(
                    startHour = hour,
                    endHour = hour + 1,
                    tasks = tasksForHour
                )
            )
        }
    }

    companion object{
        private const val HOUR_IN_MILLIS = 60*60*1000
    }
}
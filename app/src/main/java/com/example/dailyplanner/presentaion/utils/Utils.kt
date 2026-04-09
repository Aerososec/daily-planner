package com.example.dailyplanner.presentaion.utils

import com.example.dailyplanner.domain.model.Task
import java.util.Locale

fun Int.formatHourRange(endPeriod : Int) : String{
    return String.format(Locale.getDefault(), "%02d:00 - %02d:00", this, endPeriod)
}

fun tasksJoinToLine(tasks : List<Task>) : String{
    return tasks.joinToString("\n"){ task ->
        task.name
    }
}
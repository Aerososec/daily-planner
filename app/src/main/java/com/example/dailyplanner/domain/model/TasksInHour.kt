package com.example.dailyplanner.domain.model

data class TasksInHour(
    val startHour : Int,
    val endHour : Int,
    val tasks : List<Task>
)

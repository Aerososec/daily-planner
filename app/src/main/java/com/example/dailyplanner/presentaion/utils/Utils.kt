package com.example.dailyplanner.presentaion.utils

import com.example.dailyplanner.domain.model.Task
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Int.formatHourRange(endPeriod: Int): String {
    return String.format(Locale.getDefault(), "%02d:00 - %02d:00", this, endPeriod)
}

fun tasksJoinToLine(tasks: List<Task>): String {
    return tasks.joinToString("\n") { task ->
        task.name
    }
}

fun LocalDate.formatStringDate(): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
    return this.format(formatter)
}

fun LocalDate.toStartOfDay(): Long{
    return this
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}


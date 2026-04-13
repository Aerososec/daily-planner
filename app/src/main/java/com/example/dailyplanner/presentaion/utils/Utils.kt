package com.example.dailyplanner.presentaion.utils

import com.example.dailyplanner.domain.model.Task
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Int.formatHourRange(endPeriod: Int): String {
    return String.format(Locale.getDefault(), "%02d:00 - %02d:00", this, endPeriod)
}

fun Long.formatHourRange(endMillis: Long): String {
    val zone = ZoneId.systemDefault()

    val startTime = Instant.ofEpochMilli(this)
        .atZone(zone)
        .toLocalTime()

    val endTime = Instant.ofEpochMilli(endMillis)
        .atZone(zone)
        .toLocalTime()

    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    return "${startTime.format(formatter)} - ${endTime.format(formatter)}"
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

fun LocalDateTime.toMillis(): Long {
    return this
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}
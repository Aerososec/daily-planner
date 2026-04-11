package com.example.dailyplanner.domain.model

data class Task(
    val id : Long = 0,
    val dateStart : Long,
    val dateFinish : Long,
    val name : String,
    val description : String
)

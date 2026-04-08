package com.example.dailyplanner.domain.model

data class Task(
    val id : Long,
    val dateStart : Long,
    val dateFinish : Long,
    val name : String,
    val description : String
)

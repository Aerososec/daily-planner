package com.example.dailyplanner.domain.model

data class Task(
    val id : Long,
    val dateState : Long,
    val dateFinish : Long,
    val name : String,
    val description : String
)

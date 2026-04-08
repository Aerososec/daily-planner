package com.example.dailyplanner.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskDbModel(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val dateStart : Long,
    val dateFinish : Long,
    val name : String,
    val description : String
)

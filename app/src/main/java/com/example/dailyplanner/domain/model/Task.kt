package com.example.dailyplanner.domain.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("id")
    val id : Long = 0,
    @SerializedName("date_start")
    val dateStart : Long,
    @SerializedName("date_finish")
    val dateFinish : Long,
    @SerializedName("name")
    val name : String,
    @SerializedName("description")
    val description : String
)

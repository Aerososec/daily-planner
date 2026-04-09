package com.example.dailyplanner.presentaion.calendarScreen.recyclerView

import androidx.recyclerview.widget.DiffUtil
import com.example.dailyplanner.domain.model.Task
import com.example.dailyplanner.domain.model.TasksInHour

class TasksDiffCallBack : DiffUtil.ItemCallback<TasksInHour>(){
    override fun areItemsTheSame(
        oldItem: TasksInHour,
        newItem: TasksInHour
    ): Boolean {
        return oldItem.startHour == newItem.startHour
    }

    override fun areContentsTheSame(
        oldItem: TasksInHour,
        newItem: TasksInHour
    ): Boolean {
        return oldItem == newItem
    }
}
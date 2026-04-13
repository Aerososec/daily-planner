package com.example.dailyplanner.presentaion.calendarScreen.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyplanner.databinding.ItemTaskBinding
import com.example.dailyplanner.domain.model.Task
import com.example.dailyplanner.domain.model.TasksInHour
import com.example.dailyplanner.presentaion.utils.formatHourRange
import com.example.dailyplanner.presentaion.utils.tasksJoinToLine

class TasksForHourAdapter : ListAdapter<TasksInHour, TasksForHourAdapter.TasksViewHolder>(
    TasksDiffCallBack()
){

    var timePeriodClick : ((TasksInHour) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TasksViewHolder,
        position: Int
    ) {
        holder.onBind(getItem(position))

        holder.itemView.setOnClickListener {
            timePeriodClick?.invoke(getItem(position))
        }
    }


    class TasksViewHolder(binding : ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){
        private val period = binding.tvTime
        private val tasks = binding.tvTaskName

        fun onBind(tasksInHour: TasksInHour){
            period.text = tasksInHour.startHour.formatHourRange(tasksInHour.endHour)
            tasks.text = tasksJoinToLine(tasksInHour.tasks)
        }
    }
}
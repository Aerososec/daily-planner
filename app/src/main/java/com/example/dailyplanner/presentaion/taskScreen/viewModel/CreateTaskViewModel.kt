package com.example.dailyplanner.presentaion.taskScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyplanner.domain.model.Task
import com.example.dailyplanner.domain.usecase.InsertTaskUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateTaskViewModel @Inject constructor(
    private val insertTaskUseCase: InsertTaskUseCase
) : ViewModel() {

    fun insertTask(dateStart : Long, dateFinish : Long, name : String, description : String){
        val task = Task(
            dateStart = dateStart,
            dateFinish = dateFinish,
            name = name,
            description = description
        )
        viewModelScope.launch {
            insertTaskUseCase(task)
        }
    }
}
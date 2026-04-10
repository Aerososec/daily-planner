package com.example.dailyplanner.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dailyplanner.di.annotation.ViewModelKey
import com.example.dailyplanner.di.factory.ViewModelFactory
import com.example.dailyplanner.presentaion.calendarScreen.GetTasksViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory) : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GetTasksViewModel::class)
    fun bindGetTaskViewModel(getTasksViewModel: GetTasksViewModel) : ViewModel
}
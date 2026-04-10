package com.example.dailyplanner.di.component

import com.example.dailyplanner.data.db.AppDataBase
import com.example.dailyplanner.di.module.TaskRepositoryModule
import com.example.dailyplanner.di.module.ViewModelModule
import dagger.Component

@Component(
    modules = [
        AppDataBase::class,
        TaskRepositoryModule::class,
        ViewModelModule::class
    ]
)
class MainComponent {
}
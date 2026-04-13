package com.example.dailyplanner

import android.app.Application
import com.example.dailyplanner.di.component.DaggerMainComponent
import com.example.dailyplanner.di.component.MainComponent
import com.example.dailyplanner.domain.usecase.LoadInitialTasksUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DailyPlannerApp : Application() {
    lateinit var mainComponent: MainComponent

    @Inject
    lateinit var loadInitialTasksUseCase: LoadInitialTasksUseCase

    override fun onCreate() {
        super.onCreate()
        mainComponent = DaggerMainComponent.factory().create(this)
        mainComponent.inject(this)

        CoroutineScope(Dispatchers.IO).launch {
            loadInitialTasksUseCase()
        }
    }
}
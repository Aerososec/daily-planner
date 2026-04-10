package com.example.dailyplanner

import android.app.Application
import com.example.dailyplanner.di.component.DaggerMainComponent
import com.example.dailyplanner.di.component.MainComponent

class DailyPlannerApp : Application() {
    lateinit var mainComponent: MainComponent

    override fun onCreate() {
        super.onCreate()
        mainComponent = DaggerMainComponent.factory().create(this)
        mainComponent.inject(this)
    }
}
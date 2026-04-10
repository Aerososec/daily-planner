package com.example.dailyplanner.di.component

import android.app.Application
import com.example.dailyplanner.DailyPlannerApp
import com.example.dailyplanner.MainActivity
import com.example.dailyplanner.di.module.AppDataBaseModule
import com.example.dailyplanner.di.module.TaskRepositoryModule
import com.example.dailyplanner.di.module.ViewModelModule
import com.example.dailyplanner.presentaion.calendarScreen.fragment.CalendarWithTasksFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        AppDataBaseModule::class,
        TaskRepositoryModule::class,
        ViewModelModule::class
    ]
)
interface MainComponent {

    fun inject(activity: MainActivity)
    fun inject(app : DailyPlannerApp)
    fun inject(fragment : CalendarWithTasksFragment)

    @Component.Factory
    interface MainComponentFactory {
        fun create(@BindsInstance application: Application) : MainComponent
    }
}
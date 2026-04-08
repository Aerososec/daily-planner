package com.example.dailyplanner.di.component

import com.example.dailyplanner.data.db.AppDataBase
import dagger.Component

@Component(modules = [AppDataBase::class])
class MainComponent {
}
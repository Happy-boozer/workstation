package com.example.coursework

import android.app.Application
import com.example.coursework.di.AppContainer

class App : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

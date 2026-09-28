package com.example.minuta_nutricional

import android.app.Application
import android.content.Context

class MinutaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        lateinit var context: Context
            private set
    }
}

package com.openclassrooms.rebonnte.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class RebonnteApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RebonnteApplication)
            modules(appModule)
        }
    }
}
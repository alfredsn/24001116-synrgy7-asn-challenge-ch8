package com.example.hublss.appkoin

import android.app.Application
import com.example.koin.hublssModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HublssApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HublssApplication)
            modules(listOf(
                hublssModules,
                viewModelModule
            ))
        }
    }
}

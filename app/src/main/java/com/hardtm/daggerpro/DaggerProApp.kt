package com.hardtm.daggerpro

import android.app.Application
import com.hardtm.daggerpro.di.AppComponent
import com.hardtm.daggerpro.di.DaggerAppComponent

class DaggerProApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .build()
    }
}
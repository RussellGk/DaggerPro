package com.hardtm.daggerpro

import android.app.Application
import android.util.Log
import com.hardtm.daggerpro.di.AppComponent
import com.hardtm.daggerpro.di.DaggerAppComponent
import kotlin.properties.Delegates

class DaggerProApp : Application() {

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .build()

        //Start network callback
        NetworkConnectionMonitor(this).startNetworkCallback()
    }

    override fun onTerminate() {
        super.onTerminate()
        //Stop network callback
        NetworkConnectionMonitor(this).stopNetworkCallback()
    }

    companion object{
        lateinit var appComponent: AppComponent
    }
}

object NetworkState {
    var isNetworkConnected: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        Log.i("Network connectivity", "$newValue")
    }
}
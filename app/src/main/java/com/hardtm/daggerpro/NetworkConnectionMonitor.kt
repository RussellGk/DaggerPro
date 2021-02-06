package com.hardtm.daggerpro

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.widget.Toast

class NetworkConnectionMonitor(private val application: Application){

    fun startNetworkCallback() {
        val cm: ConnectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        cm.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    NetworkState.isNetworkConnected = true
                    Toast.makeText(application, application.getText(R.string.online_mode), Toast.LENGTH_LONG).show()
                }

                override fun onLost(network: Network) {
                    NetworkState.isNetworkConnected = false
                    Toast.makeText(application, application.getText(R.string.offline_mode), Toast.LENGTH_LONG).show()
                }
            })
    }

    fun stopNetworkCallback() {
        val cm: ConnectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
    }
}
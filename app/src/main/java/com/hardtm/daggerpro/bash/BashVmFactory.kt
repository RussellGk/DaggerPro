package com.hardtm.daggerpro.bash

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hardtm.daggerpro.rest.BashAPI

class BashVmFactory(val application: Application, val injectedBashApi: BashAPI) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BashViewModel(application, injectedBashApi) as T
    }
}
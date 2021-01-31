package com.hardtm.daggerpro.jokes

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hardtm.daggerpro.rest.JokeAPI

class JokesVmFactory(val application: Application, val injectedJokeApi: JokeAPI) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return JokeViewModel(application, injectedJokeApi) as T
    }
}
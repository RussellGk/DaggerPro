package com.hardtm.daggerpro.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hardtm.daggerpro.db.*

class JokeViewModel(application: Application) : AndroidViewModel(application) {

    var database: DaggerProDatabase? = null
    var jokeList: LiveData<List<JokeEntity>>? = null

    init {
        val jokeDao = database?.jokeDao()
        jokeList = jokeDao?.getJokeList()
    }
}
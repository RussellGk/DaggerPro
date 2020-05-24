package com.hardtm.daggerpro.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hardtm.daggerpro.db.*

class JokeViewModel(application: Application) : AndroidViewModel(application) {

    val jokeList: LiveData<List<JokeEntity>>
    val jokeDao: JokeDao

    init {
        jokeDao = DaggerProDatabase.getDatabase(application).jokeDao()
        jokeList = jokeDao.getJokeList()
    }
}
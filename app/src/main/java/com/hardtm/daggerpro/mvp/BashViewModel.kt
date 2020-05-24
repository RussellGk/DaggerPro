package com.hardtm.daggerpro.mvp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hardtm.daggerpro.db.BashDao
import com.hardtm.daggerpro.db.BashEntity
import com.hardtm.daggerpro.db.DaggerProDatabase

class BashViewModel(application: Application) : AndroidViewModel(application) {

    val bashList: LiveData<List<BashEntity>>
    val bashDao: BashDao

    init {
        bashDao = DaggerProDatabase.getDatabase(application).bashDao()
        bashList = bashDao.getBashList()
    }
}
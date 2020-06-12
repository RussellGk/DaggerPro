package com.hardtm.daggerpro.features.bash

import com.hardtm.daggerpro.features.bash.models.BashDataModel

interface BashLocalDataSource {

    suspend fun fetchBashes(): List<BashDataModel>
    suspend fun saveData(bashes: List<BashDataModel>)
    suspend fun clear()
}
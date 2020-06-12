package com.hardtm.daggerpro.features.bash

import com.hardtm.daggerpro.features.bash.models.BashDataModel

interface BashRemoteDataSource {

    suspend fun fetchBashes(): List<BashDataModel>
}
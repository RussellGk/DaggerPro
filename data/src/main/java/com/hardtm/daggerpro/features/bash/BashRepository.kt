package com.hardtm.daggerpro.features.bash

import com.hardtm.daggerpro.features.bash.models.BashDataModel

interface BashRepository {

    suspend fun getBashes(): List<BashDataModel>
}
package com.hardtm.daggerpro.features.bash.mock_local

import com.hardtm.daggerpro.features.bash.BashLocalDataSource
import com.hardtm.daggerpro.features.bash.models.BashDataModel

class MockBashLocalDataSource: BashLocalDataSource {

    override suspend fun fetchBashes(): List<BashDataModel> {
        return emptyList()
    }

    override suspend fun saveData(bashes: List<BashDataModel>) {

    }

    override suspend fun clear() {

    }
}
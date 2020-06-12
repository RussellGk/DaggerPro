package com.hardtm.daggerpro.features.bash

import com.hardtm.daggerpro.features.bash.models.BashDataModel
import javax.inject.Inject

class BashRepositoryImpl @Inject constructor(
    private val remoteDataSource: BashRemoteDataSource,
    private val localDataSource: BashLocalDataSource
): BashRepository {

    override suspend fun getBashes(): List<BashDataModel> {
        val localBashes = localDataSource.fetchBashes()
        return if (localBashes.isEmpty()) {
            val remoteBashes = remoteDataSource.fetchBashes()
            localDataSource.clear()
            localDataSource.saveData(remoteBashes)
            remoteBashes
        } else {
            localBashes
        }
    }
}
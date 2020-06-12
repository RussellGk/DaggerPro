package com.hardtm.daggerpro.features.bash.retrofit

import com.hardtm.daggerpro.features.bash.BashRemoteDataSource
import com.hardtm.daggerpro.features.bash.models.BashDataModel
import javax.inject.Inject

class RetrofitBashRemoteDataSource @Inject constructor(
    private val bashApi: BashAPI
): BashRemoteDataSource {

    override suspend fun fetchBashes(): List<BashDataModel> {
        return bashApi.getBash(site = "", name = "", num = "")
    }
}
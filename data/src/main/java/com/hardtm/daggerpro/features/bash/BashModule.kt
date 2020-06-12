package com.hardtm.daggerpro.features.bash

import com.hardtm.daggerpro.features.bash.mock_local.MockBashLocalDataSource
import com.hardtm.daggerpro.features.bash.retrofit.BashAPI
import com.hardtm.daggerpro.features.bash.retrofit.RetrofitBashRemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class BashModule {

    @Provides
    fun provideRemoteDataSource(bashApi: BashAPI): BashRemoteDataSource =
        RetrofitBashRemoteDataSource(bashApi)

    @Provides
    fun provideLocalDataSource(): BashLocalDataSource =
        MockBashLocalDataSource()
}
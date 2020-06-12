package com.hardtm.daggerpro.features.bash.retrofit

import com.google.gson.annotations.SerializedName
import com.hardtm.daggerpro.features.bash.models.BashDataModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BashAPI {
    @GET("get")
    suspend fun getBash(
        @Query("site") site: String,
        @Query("name") name: String,
        @Query("num") num: String
    ): List<BashDataModel>
}
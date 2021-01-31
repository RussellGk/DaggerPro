package com.hardtm.daggerpro.rest

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BashAPI {
    @GET("get")
    fun getBash(
        @Query("site") site: String,
        @Query("name") name: String,
        @Query("num") num: String
    ): Single<Response<List<BashListModel>>>
}

data class BashListModel(
    @SerializedName("site") val site: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("desc") val desc: String?,
    @SerializedName("elementPureHtml") val elementPureHtml: String?
)
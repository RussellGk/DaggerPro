package com.hardtm.daggerpro.features.jokes.retrofit

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JokeAPI {
    @GET("get")
    fun getJokes(
        @Query("site") site: String,
        @Query("name") name: String,
        @Query("num") num: String
    ): Observable<Response<List<JokeListModel>>>
}

data class JokeListModel(
    @SerializedName("site") val site: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("desc") val desc: String?,
    @SerializedName("elementPureHtml") val elementPureHtml: String?
)

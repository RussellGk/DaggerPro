package com.hardtm.daggerpro.rest

import com.hardtm.daggerpro.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class JokeService(
    retrofit: Retrofit = Retrofit.Builder()
        .client(OkHttpClient().newBuilder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(15, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                })
        }.build())
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

) {
    val api: JokeAPI = retrofit.create(JokeAPI::class.java)

    companion object {
        private const val baseUrl = "http://umorili.herokuapp.com/api/"
    }
}
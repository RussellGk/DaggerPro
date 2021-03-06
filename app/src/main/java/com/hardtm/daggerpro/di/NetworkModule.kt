package com.hardtm.daggerpro.di

import com.hardtm.daggerpro.BuildConfig
import com.hardtm.daggerpro.rest.BashAPI
import com.hardtm.daggerpro.rest.JokeAPI
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    private val baseUrl = "http://umorili.herokuapp.com/api/"

    @Provides
    fun provideOkHttp(): OkHttpClient =
        OkHttpClient.Builder()
            .apply {
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(15, TimeUnit.SECONDS)
                writeTimeout(15, TimeUnit.SECONDS)
                addInterceptor(
                    HttpLoggingInterceptor()
                        .apply {
                            if (BuildConfig.DEBUG) {
                                level = HttpLoggingInterceptor.Level.BODY
                            }
                        })
            }
            .build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideJokeApi(retrofit: Retrofit): JokeAPI =
        retrofit.create(JokeAPI::class.java)

    @Provides
    @Singleton
    fun provideBashApi(retrofit: Retrofit): BashAPI =
        retrofit.create(BashAPI::class.java)
}
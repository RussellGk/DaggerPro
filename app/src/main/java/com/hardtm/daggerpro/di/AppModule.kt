package com.hardtm.daggerpro.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Singleton
    @Binds   // @Binds, binds the Application instance to Context
    abstract fun context(appInstance: Application): Context
}
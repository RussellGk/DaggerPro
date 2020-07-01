package com.hardtm.daggerpro.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hardtm.daggerpro.db.DaggerProDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): DaggerProDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            DaggerProDatabase::class.java,
            "Dagger_pro_database"
        ).fallbackToDestructiveMigration().build()

}
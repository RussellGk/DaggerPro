package com.hardtm.daggerpro.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(BashEntity::class, JokeEntity::class), version = 1)//exportSchema = false
abstract class DaggerProDatabase: RoomDatabase() {
    abstract fun bashDao(): BashDao
    abstract fun jokeDao(): JokeDao

    companion object {
        @Volatile
        private var INSTANCE: DaggerProDatabase? = null

        fun getDatabase(context: Context): DaggerProDatabase {
            val currentInstance = INSTANCE
            if (currentInstance != null) {
                return currentInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DaggerProDatabase::class.java,
                    "Dagger_pro_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
package com.hardtm.daggerpro.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BashEntity::class, JokeEntity::class], version = 1)//exportSchema = false
abstract class DaggerProDatabase: RoomDatabase() {
    abstract fun bashDao(): BashDao
    abstract fun jokeDao(): JokeDao
}
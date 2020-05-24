package com.hardtm.daggerpro.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface JokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveJokeList(jokeList: List<JokeEntity>)

    @Query("SELECT * FROM  joke_db")
    fun getJokeList(): LiveData<List<JokeEntity>>

    @Query("DELETE FROM joke_db")
    fun deleteJokeList()
}
package com.hardtm.daggerpro.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BashDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBashList(bashList: List<BashEntity>)

    @Query("SELECT * FROM  bash_db")
    fun getBashList(): LiveData<List<BashEntity>>

    @Query("DELETE FROM bash_db")
    fun deleteBashList()
}
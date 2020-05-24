package com.hardtm.daggerpro.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bash_db")
class BashEntity (
    @PrimaryKey
    @ColumnInfo(name = "bash_id")
    val bashId: Int,

    @ColumnInfo(name = "bash_text")
    val bashText: String
)
package com.hardtm.daggerpro.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "joke_db")
class JokeEntity (
    @PrimaryKey
    @ColumnInfo(name = "joke_id")
    val jokeId: Int,

    @ColumnInfo(name = "joke_text")
    val jokeText: String
)
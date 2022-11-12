package com.sfu362group2.musicistrivial.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
class GameHistory (
    @PrimaryKey(autoGenerate = true)
    var date: Long = 0,

    @ColumnInfo(name =  "day_score")
    var score: Int = 0,

    @ColumnInfo(name = "consecutive_days_played")
    var streak: Int = 0

)
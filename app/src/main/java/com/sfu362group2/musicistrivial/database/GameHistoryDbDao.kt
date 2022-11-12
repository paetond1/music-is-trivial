package com.sfu362group2.musicistrivial.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameHistoryDbDao {
    @Insert fun insertDailyEntry(gameHistory: GameHistory)

    @Query("SELECT * FROM history_table")
    fun getAllEntries(): Flow<List<GameHistory>>

    @Query("DELETE FROM history_table WHERE date = :key")
    suspend fun deleteDailyEntry(key: Long)

    @Query("DELETE FROM history_table")
    suspend fun deleteAllEntries()

    @Query("SELECT * FROM history_table WHERE date = :key")
    fun getDailyEntry(key: Long): LiveData<GameHistory>

    @Query("SELECT consecutive_days_played  FROM history_table WHERE date = (SELECT MAX(date) FROM history_table)")
    fun getCurrentStreak() : LiveData<Int>

    @Query("SELECT MAX(consecutive_days_played) FROM history_table")
    fun getLongestStreak() : LiveData<Int>

    //TODO add total games played
    @Query("SELECT COUNT(*) FROM history_table")
    fun getTotalGamesPlayed() : Int
}
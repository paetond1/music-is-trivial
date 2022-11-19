package com.sfu362group2.musicistrivial.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameHistoryDbDao {
    @Insert
    fun insertDailyEntry(gameHistory: GameHistory)

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

    @Query ("SELECT SUM(day_score) FROM history_table")
    fun getTotalScore() : LiveData<Float>

    @Query("SELECT COUNT(*) FROM history_table")
    fun getTotalGamesPlayed() : LiveData<Int>

    @Query("SELECT AVG(day_score) FROM history_table")
    fun getAvgScore() : LiveData<Float>

    @Query("SELECT COUNT(*) FROM history_table WHERE day_score = 0.0")
    fun getTotalZeroScoreGames() : LiveData<Int>

    @Query("SELECT COUNT(*) FROM history_table WHERE day_score = 5.0")
    fun getPerfectScore() : LiveData<Int>
}
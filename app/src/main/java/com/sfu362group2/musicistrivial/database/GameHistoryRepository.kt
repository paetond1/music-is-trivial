package com.sfu362group2.musicistrivial.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class GameHistoryRepository(private val gameHistoryDbDao: GameHistoryDbDao) {
    val allEntries: Flow<List<GameHistory>> = gameHistoryDbDao.getAllEntries()
    val lastEntry: Flow<GameHistory> = gameHistoryDbDao.getLastEntry()
    val currentStreak: Flow<Int> = gameHistoryDbDao.getCurrentStreak()
    val totalScore: Flow<Float> = gameHistoryDbDao.getTotalScore()
    val longestStreak: Flow<Int> = gameHistoryDbDao.getLongestStreak()
    val avgScore: Flow<Float> = gameHistoryDbDao.getAvgScore()
    val totalGamesPlayed: Flow<Int> = gameHistoryDbDao.getTotalGamesPlayed()
    val perfectScoredGames: Flow<Int> = gameHistoryDbDao.getPerfectScore()
    val zeroScoredGames: Flow<Int> = gameHistoryDbDao.getTotalZeroScoreGames()

    @WorkerThread
    suspend fun insertEntry(gameHistory: GameHistory) {
        gameHistoryDbDao.insertDailyEntry(gameHistory)
    }

    @WorkerThread
    suspend fun deleteEntry(key: Long) {
        gameHistoryDbDao.deleteDailyEntry(key)
    }

    @WorkerThread
    suspend fun deleteAllEntries() {
        gameHistoryDbDao.deleteAllEntries()
    }

    fun getDailyEntry(key: Long): Flow<GameHistory> {
        return gameHistoryDbDao.getDailyEntry(key)
    }

}
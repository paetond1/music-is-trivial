package com.sfu362group2.musicistrivial.database

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GameHistoryRepository(private val gameHistoryDbDao: GameHistoryDbDao) {
    val allEntries: Flow<List<GameHistory>> = gameHistoryDbDao.getAllEntries()

    fun insertEntry(gameHistory: GameHistory) {
        CoroutineScope(IO).launch {
            gameHistoryDbDao.insertDailyEntry(gameHistory)
        }
    }

    fun deleteEntry(key: Long) {
        CoroutineScope(IO).launch {
            gameHistoryDbDao.deleteDailyEntry(key)
        }
    }


    fun deleteAllEntries() {
        CoroutineScope(IO).launch{
            gameHistoryDbDao.deleteAllEntries()
        }
    }

    fun getDailyEntry(key: Long) : LiveData<GameHistory> {
        return gameHistoryDbDao.getDailyEntry(key)
    }

    fun getCurrentStreak() : LiveData<Int> {
        return gameHistoryDbDao.getCurrentStreak()
    }

    fun getTotalScore() : LiveData<Int> {
        return gameHistoryDbDao.getTotalScore()
    }

    fun getLongestStreak(): LiveData<Int> {
        return gameHistoryDbDao.getLongestStreak()
    }

    fun getAvgScore() : LiveData<Float> {
        return gameHistoryDbDao.getAvgScore()
    }

    fun getTotalGamesPlayed() : LiveData<Int> {
        return gameHistoryDbDao.getTotalGamesPlayed()
    }

    fun getTotalZeroScoreGames() : LiveData<Int> {
        return gameHistoryDbDao.getTotalZeroScoreGames()
    }
}
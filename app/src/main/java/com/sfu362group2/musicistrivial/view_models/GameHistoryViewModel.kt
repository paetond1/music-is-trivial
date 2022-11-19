package com.sfu362group2.musicistrivial.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.sfu362group2.musicistrivial.database.GameHistory
import com.sfu362group2.musicistrivial.database.GameHistoryRepository

class GameHistoryViewModel(private val repository: GameHistoryRepository) : ViewModel() {
    val allGameHistoryLiveData = repository.allEntries.asLiveData()

    fun insertEntry(gameHistoryEntry: GameHistory) {
        repository.insertEntry(gameHistoryEntry)
    }

    fun deleteEntry(id: Long) {
        repository.deleteEntry(id)
    }

    fun deleteAllEntries() {
        repository.deleteAllEntries()
    }

    fun getHistoryEntry(id: Long) : LiveData<GameHistory> {
        return repository.getDailyEntry(id)
    }

    fun getCurrentStreak() : LiveData<Int> {
        return repository.getCurrentStreak()
    }

    fun getLongestStreak() : LiveData<Int> {
        return repository.getLongestStreak()
    }

    fun getTotalScore() : LiveData<Float> {
        return repository.getTotalScore()
    }

    fun getAvgScore() : LiveData<Float> {
        return repository.getAvgScore()
    }

    fun getTotalGamesPlayed() : LiveData<Int> {
        return repository.getTotalGamesPlayed()
    }

    fun getTotalZeroScoreGames() : LiveData<Int> {
        return repository.getTotalZeroScoreGames()
    }
    fun getPerfectScore() : LiveData<Int> {
        return repository.getPerfectScore()
    }



    class GameHistoryViewModelFactory(private val repository: GameHistoryRepository)
        : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(GameHistoryViewModel::class.java))
                return GameHistoryViewModel(repository) as T
            throw java.lang.IllegalArgumentException("Error")
        }
    }
}
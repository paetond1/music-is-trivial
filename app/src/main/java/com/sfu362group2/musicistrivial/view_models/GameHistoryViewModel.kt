package com.sfu362group2.musicistrivial.view_models

import androidx.lifecycle.*
import com.sfu362group2.musicistrivial.database.GameHistory
import com.sfu362group2.musicistrivial.database.GameHistoryRepository
import kotlinx.coroutines.launch
import java.util.*

class GameHistoryViewModel(private val repository: GameHistoryRepository) : ViewModel() {

    val allGameHistoryLiveData = repository.allEntries.asLiveData()
    val lastEntryLiveData = repository.lastEntry.asLiveData()
    val currentStreakLiveData = repository.currentStreak.asLiveData()
    val totalScoreLiveData = repository.totalScore.asLiveData()
    val longestStreakLiveData = repository.longestStreak.asLiveData()
    val avgScoreLiveData = repository.avgScore.asLiveData()
    val totalGamesPlayedLiveData = repository.totalGamesPlayed.asLiveData()
    val perfectScoredGamesLiveData = repository.perfectScoredGames.asLiveData()
    val zeroScoredGamesLiveData = repository.zeroScoredGames.asLiveData()


    fun insertEntry(gameHistoryEntry: GameHistory) = viewModelScope.launch {
        var counter = 0
        repository.lastEntry.collect { previousEntry ->
            if (previousEntry != null) {
                if ((gameHistoryEntry.date?.minus(previousEntry.date!!))!! == 1L) {
                    gameHistoryEntry.streak = previousEntry.streak + 1
                } else {
                    gameHistoryEntry.streak = 1
                }
                if (counter == 0)
                    repository.insertEntry(gameHistoryEntry)
                counter++
            }
            else {
                gameHistoryEntry.streak = 1
                counter++
                repository.insertEntry(gameHistoryEntry)
            }
        }
    }

    fun deleteEntry(id: Long) = viewModelScope.launch {
        repository.deleteEntry(id)
    }

    fun deleteAllEntries() = viewModelScope.launch{
        repository.deleteAllEntries()
    }

    fun getHistoryEntry(id: Long) : LiveData<GameHistory> {
        return repository.getDailyEntry(id).asLiveData()
    }

}

class GameHistoryViewModelFactory(private val repository: GameHistoryRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameHistoryViewModel(repository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
    }
}
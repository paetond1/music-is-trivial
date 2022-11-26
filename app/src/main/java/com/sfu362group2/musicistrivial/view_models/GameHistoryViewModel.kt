package com.sfu362group2.musicistrivial.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.sfu362group2.musicistrivial.database.GameHistory
import com.sfu362group2.musicistrivial.database.GameHistoryRepository

class GameHistoryViewModel(private val repository: GameHistoryRepository) : ViewModel() {
    val allGameHistoryLiveData = repository.allEntries.asLiveData()

    fun insertEntry(gameHistoryEntry: GameHistory) {

        //TODO - Figure out how to get the find if yesterday (gameHistoryEntry.date - 1) has an entry.
        // If it has an entry, get the streak from that entry and set this gameHistoryEntry streak to one higher
//        Log.i("DEBUG: ", "score: ${gameHistoryEntry.score} date: ${gameHistoryEntry.date}")
//        val streak = streakYesterday(gameHistoryEntry.date!!)
//        Log.i("DEBUG: ", "STREAK BEFORE TODAY IS $streak")
//        if (streak != null){
//            gameHistoryEntry.streak = streak + 1
//        }
//        else{
//            gameHistoryEntry.streak = 1
//        }
        repository.insertEntry(gameHistoryEntry)
    }

//    private fun streakYesterday(todayDate : Long) : Int?{
//        val entry = getHistoryEntry(todayDate - 1)
//        return entry.value?.streak
//    }

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
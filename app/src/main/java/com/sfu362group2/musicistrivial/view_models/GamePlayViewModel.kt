package com.sfu362group2.musicistrivial.view_models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sfu362group2.musicistrivial.game_logic.Game

private const val TAG = "DEBUG: GamePlayViewModel - "

class GamePlayViewModel : ViewModel() {
    companion object {
        const val MAX_CHOSEN_SONGS = 5
    }

    val game = MutableLiveData<Game>()
    var shuffledSongs = MutableLiveData<ArrayList<Game.Song>>()
    private val rankCounter = MutableLiveData(0)

    fun setGame(
        date: Long,
        artistName: String,
        allSongsInOrder: ArrayList<String>
    ) {
        // ensure game only initialized once
        if (game.value == null) {
            this.game.value = Game(date, artistName, allSongsInOrder)
            this.shuffledSongs.value = game.value!!.getSongOptions()
        }
    }

    fun clearRanks() {
        if (shuffledSongs.value != null && game.value != null) {
            rankCounter.value = 0
            game.value!!.clearSubmittedSongs()
            for (song in shuffledSongs.value!!) {
                song.input_rank = 0
            }

        }
    }

    fun rankSong(position: Int) {
        if (shuffledSongs.value != null && game.value != null){
            if (shuffledSongs.value!![position].input_rank == 0 && rankCounter.value!! < MAX_CHOSEN_SONGS) {
                rankCounter.value = rankCounter.value!! + 1
                shuffledSongs.value!![position].input_rank = rankCounter.value!!
                val songTitleRanked = shuffledSongs.value!![position].song_title
                game.value!!.addSongToSubmit(songTitleRanked)
                Log.i(TAG, "Submit List: ${game.value!!.getSubmittedSongs()}")
                Log.i(TAG, "Correct List: ${game.value!!.getCorrectSongs()}")
            }
        }
    }

}
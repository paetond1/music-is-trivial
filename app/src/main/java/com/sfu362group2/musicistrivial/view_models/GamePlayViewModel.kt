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
    val shuffledSongs = MutableLiveData<ArrayList<Game.Song>>()
    val rankCounter: Int get() { return rankedSet.size }

    private var unrankedSet: MutableSet<Int> = mutableSetOf(1, 2, 3, 4, 5)
    private var rankedSet: MutableSet<Int> = mutableSetOf()
    private var songsForSubmission: ArrayList<String> = arrayListOf("","","","","")

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
            unrankedSet = mutableSetOf(1, 2, 3, 4, 5)
            rankedSet = mutableSetOf()
            game.value!!.clearSubmittedSongs()
            for (song in shuffledSongs.value!!) {
                song.input_rank = 0
            }
            for (i in 0 until songsForSubmission.size){
                songsForSubmission[i] = ""
            }
        }
    }

    fun rankSong(position: Int) {
        if (shuffledSongs.value != null && game.value != null){
            // rank an unselected song
            if (shuffledSongs.value!![position].input_rank == 0 && rankCounter < MAX_CHOSEN_SONGS) {
                val pop = unrankedSet.min()
                unrankedSet.remove(pop)
                rankedSet.add(pop)
                shuffledSongs.value!![position].input_rank = pop
                val songTitleRanked = shuffledSongs.value!![position].song_title
                songsForSubmission[pop-1] = songTitleRanked
            // deselect a ranked song
            } else if (shuffledSongs.value!![position].input_rank != 0) {
                val pop = shuffledSongs.value!![position].input_rank
                unrankedSet.add(pop)
                rankedSet.remove(pop)
                shuffledSongs.value!![position].input_rank = 0
                songsForSubmission[pop-1] = ""
            }
            Log.i(TAG, "Submit List: $songsForSubmission")
            Log.i(TAG, "Correct List: ${game.value!!.getCorrectSongs()}")
        }
    }

    fun submitSongs() : Array<Float>? {
        return if (rankCounter == 5){
            for (i in 0..4) {
                game.value!!.addSongToSubmit(songsForSubmission[i])
            }
            game.value!!.submitSongs()
        } else{

            null
        }
    }

}
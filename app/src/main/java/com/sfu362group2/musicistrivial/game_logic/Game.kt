package com.sfu362group2.musicistrivial.game_logic

import android.util.Log

private const val TAG = "Game Class: "

class Game(date: Long, artistName: String, allSongsInOrder: ArrayList<String>)  {
    private val date: Long
    private val artistName: String
    private val allSongsInOrder: List<String>
    private val songOptions : List<Song>
    private val correctSongs: ArrayList<String>
    private var submittedSongs: ArrayList<String>
    private var detailedScore: Array<Float>

    init {
        this.date = date
        this.artistName = artistName
        this.allSongsInOrder = allSongsInOrder
        this.correctSongs = ArrayList(allSongsInOrder.subList(0, 5))
        this.detailedScore = arrayOf(-1.0f, -1.0f, -1.0f, -1.0f, -1.0f)
        this.submittedSongs = ArrayList()
        this.songOptions = ArrayList()

        val shuffledSongStrings = allSongsInOrder.shuffled()
        for (str in shuffledSongStrings){
            this.songOptions.add(Song(str))
        }
    }


    fun getCorrectSongs(): ArrayList<String> {
        return correctSongs
    }

    fun getArtistName(): String {
        return this.artistName
    }

    fun getSongOptions(): ArrayList<Song> {
        return this.songOptions as ArrayList<Song>
    }

    fun getSubmittedSongs(): List<String> {
        return this.submittedSongs
    }

    // checks the submitted songs against correct songs and outputs detailed score list
    fun submitSongs(): Array<Float>? {
        if (submittedSongs.size != 5) {
            return null
        }
        for (i in 0..4) {
            if (this.correctSongs[i] == submittedSongs[i]) {
                detailedScore[i] = 1.0f
            } else if (isInSubmittedSongs(correctSongs[i])) {
                detailedScore[i] = 0.5f
            } else {
                detailedScore[i] = 0.0f
            }
        }
        return detailedScore
    }

    fun addSongToSubmit(songTitle: String){
        if (this.submittedSongs.size < 5){
            this.submittedSongs.add(songTitle)
        }
        else{
            Log.e(TAG, "Could not add song to submit, too many songs")
        }
    }


    fun removeSongToSubmit(songTitle: String) {
        if (this.submittedSongs.size == 0) {
            Log.e(TAG, "No more song to remove.")
        } else {
            try {
                submittedSongs.remove(songTitle)
            } catch (e : Exception) {
                Log.e(TAG, "Error removing song: ${e.message}")
            }
        }
    }

    fun clearSubmittedSongs(){
        this.submittedSongs.clear()
    }


    fun getDate(): Long {
        return this.date
    }

    private fun isInSubmittedSongs(song: String) : Boolean{
        for (str in submittedSongs){
            if (song == str){
                return true
            }
        }
        return false
    }

    inner class Song(val song_title: String, var input_rank: Int = 0)
}
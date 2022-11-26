package com.sfu362group2.musicistrivial.game_logic

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

private const val TAG = "Game Class: "

class Game(date: Long, artistName: String, allSongsInOrder: ArrayList<String>) : Parcelable {
    private val date: Long
    private val artistName: String
    private val allSongsInOrder: List<String>
    private val songOptions : List<Song>
    private val correctSongs: List<String>
    private var score: Float
    private var submittedSongs: ArrayList<String>
    private var detailedScore: ArrayList<Float>

    constructor(parcel: Parcel) : this(
        TODO("date"),
        TODO("artistName"),
        TODO("allSongsInOrder")
    ) {
        score = parcel.readFloat()
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Game> {
        override fun createFromParcel(parcel: Parcel): Game {
            return Game(parcel)
        }

        override fun newArray(size: Int): Array<Game?> {
            return arrayOfNulls(size)
        }
    }

    init {
        this.date = date
        this.artistName = artistName
        this.allSongsInOrder = allSongsInOrder
        this.correctSongs = allSongsInOrder.subList(0, 5)
        this.score = 0.0f
        this.detailedScore = arrayListOf(-1.0f, -1.0f, -1.0f, -1.0f, -1.0f)
        this.submittedSongs = ArrayList()
        this.songOptions = ArrayList()

        val shuffledSongStrings = allSongsInOrder.shuffled()
        for (str in shuffledSongStrings){
            this.songOptions.add(Song(str))
        }
    }


    fun getCorrectSongs(): List<String> {
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

    fun getScore(): Float {
        return this.score
    }

    fun getDetailedScore(): List<Float> {
        return this.detailedScore
    }

    fun submitSongs(submittedSongs: ArrayList<String>): Int {
        if (submittedSongs.size != 5) {
            return -1
        }
        this.submittedSongs = submittedSongs
        for (i in 0..4) {
            if (this.correctSongs[i] == submittedSongs[i]) {
                detailedScore[i] = 1.0f
            } else if (submittedSongs[i].contains(this.correctSongs[i])) {
                detailedScore[i] = 0.5f
            } else {
                detailedScore[i] = 0.0f
            }
        }
        return 0
    }

    fun addSongToSubmit(songTitle: String){
        if (this.submittedSongs.size < 5){
            this.submittedSongs.add(songTitle)
        }
        else{
            Log.e(TAG, "Could not add song to submit, too many songs")
        }
    }

    fun clearSubmittedSongs(){
        this.submittedSongs.clear()
    }

    fun getDate(): Long {
        return this.date
    }

    inner class Song(val song_title: String, var input_rank: Int = 0)
}
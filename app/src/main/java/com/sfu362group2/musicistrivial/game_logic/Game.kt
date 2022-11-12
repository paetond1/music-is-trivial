package com.sfu362group2.musicistrivial.game_logic

class Game(date: String, artistName: String, allSongsInOrder: ArrayList<String>) {
    private val date: String
    private val artistName: String
    private val allSongsInOrder: List<String>
    private val songOptions : List<String>
    private val correctSongs: List<String>
    private var score: Float
    private var submittedSongs: List<String>?
    private var detailedScore: ArrayList<Float>

    init {
        this.date = date
        this.artistName = artistName
        this.allSongsInOrder = allSongsInOrder
        this.songOptions = allSongsInOrder.shuffled()
        this.correctSongs = allSongsInOrder.subList(0, 5)
        this.score = 0.0f
        this.detailedScore = arrayListOf(-1.0f, -1.0f, -1.0f, -1.0f, -1.0f)
        this.submittedSongs = null
    }

    fun getCorrectSongs(): List<String> {
        return correctSongs
    }

    fun getArtistName(): String {
        return this.artistName
    }

    fun getSongOptions(): List<String> {
        return this.songOptions
    }

    fun getSubmittedSongs(submittedSongs: List<String>): List<String> {
        return submittedSongs
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


    fun getDate(): String {
        return this.date
    }
}
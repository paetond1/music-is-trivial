package com.sfu362group2.musicistrivial.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sfu362group2.musicistrivial.activities.GamePlayActivity
import com.sfu362group2.musicistrivial.game_logic.Game

class GamePlayViewModel : ViewModel() {
//    val artistName = MutableLiveData<String>()
//    val songOptions = MutableLiveData<List<String>>(ArrayList())
//    val listItemsClicked = MutableLiveData<ArrayList<Int>>(ArrayList())
    val game = MutableLiveData<Game>()
    var shuffledSongs = MutableLiveData<ArrayList<GamePlayActivity.Song>>()

    fun setGame(
        date: String,
        artistName: String,
        allSongsInOrder: ArrayList<String>,
        shuffledSongs: ArrayList<GamePlayActivity.Song>
    ) {
        // ensure game only initialized once
        if (game.value == null){
            this.game.value = Game(date, artistName, allSongsInOrder)
//            this.artistName.value = artistName
//            this.songOptions.value = this.game.value!!.getSongOptions()
        }
        if (shuffledSongs == null) {this.shuffledSongs.value = shuffledSongs }
    }
}
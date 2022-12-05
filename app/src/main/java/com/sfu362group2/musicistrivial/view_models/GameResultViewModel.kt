package com.sfu362group2.musicistrivial.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameResultViewModel: ViewModel() {

    val artistName = MutableLiveData<String>()
    val correctSongs = MutableLiveData<ArrayList<String>>()
    val detailedScore = MutableLiveData<FloatArray>()

}
package com.sfu362group2.musicistrivial.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.RequestQueue
import com.sfu362group2.musicistrivial.api.Spotify
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class MainViewModel : ViewModel() {

    val date = MutableLiveData<String>()
    val artistImgUrl = MutableLiveData<String>()
    val artistName = MutableLiveData<String>()
    val artistId = MutableLiveData<String>()

    fun setDate() {
        date.value = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)
    }

    fun spotifyCalls(spotify: Spotify, queue: RequestQueue) {
        queue.add(spotify.tokenRequest { response ->
            val token = response.getString("access_token")
            val str = "ABCDEFGHIJKLMNOPQRSTUV"
            val char = str.random()
            queue.add(spotify.randomArtistRequest(token, char) { response ->
                val randInt = Random(System.currentTimeMillis()).nextInt(0, 20)
                val id =
                    response.getJSONObject("artists").getJSONArray("items").getJSONObject(randInt)
                        .getString("id")
                artistId.value = id
                queue.add(spotify.artistRequest(token, id) { response ->
                    artistName.value = response.getString("name")
                    artistImgUrl.value =
                        response.getJSONArray("images").getJSONObject(0).getString("url")
                })
            })
        })
    }
}
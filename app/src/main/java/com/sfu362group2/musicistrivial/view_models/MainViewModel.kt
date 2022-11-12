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
    val allSongsInOrder = MutableLiveData<ArrayList<String>>(ArrayList())

    private fun setDate() {
        date.value = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)
    }

    fun spotifyCalls(spotify: Spotify, queue: RequestQueue) {
        setDate()
        allSongsInOrder.value!!.clear()
        // Search request spotify for a random artist and get their id
        queue.add(spotify.tokenRequest { response ->
            val token = response.getString("access_token")
            queue.add(
                spotify.randomArtistRequest(
                    token,
                    randChar(date.value!!.toInt())
                ) { response ->
                    // Request artist name and image
                    val id =
                        response.getJSONObject("artists").getJSONArray("items")
                            .getJSONObject(randInt(date.value!!.toInt()))
                            .getString("id")
                    artistId.value = id
                    queue.add(spotify.artistRequest(token, id) { response ->
                        artistName.value = response.getString("name")
                        artistImgUrl.value =
                            response.getJSONArray("images").getJSONObject(0).getString("url")
                    })
                    // Request top tracks
                    queue.add(spotify.topTracksRequest(token, id){
                        response ->
                        val tracks = response.getJSONArray("tracks")
                        for (i in 0..9){
                            allSongsInOrder.value!!.add(tracks.getJSONObject(i).getString("name"))
                        }
                    })
                })
        })
    }

    /*
        Outputs a random character from 'A' to 'Z', to be used for searching in searching a random artist
        Uses the input date integer as YYYYMMDD for the seed so that devices playing the game on the same day
        make the same search query
    */
    private fun randChar(date: Int): Char {
        return Random(date).nextInt(65, 91).toChar()
    }

    /*
        A spotify search Request will return an array of artist objects. Use this function to get a
        random index for that array access. Uses the input date integer as YYYYMMDD for the seed so that
        devices playing the game on the same day select the same artist.
     */
    private fun randInt(date: Int): Int {
        return Random(date).nextInt(0, 20)
    }
}
package com.sfu362group2.musicistrivial.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.sfu362group2.musicistrivial.R
import com.sfu362group2.musicistrivial.api.Spotify
import com.squareup.picasso.Picasso
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var artistImg: ImageView
    private lateinit var artistName: TextView
    private lateinit var playButton: Button
    private lateinit var instructionButton: Button
    private lateinit var statButton: Button
    private lateinit var loginButton: Button
    private lateinit var queue: RequestQueue
    private lateinit var spotify: Spotify

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playButton = findViewById(R.id.button_play)
        playButton.setOnClickListener {
            val i = Intent(this, GamePlayActivity::class.java)
            startActivity(i)
        }

        instructionButton = findViewById(R.id.button_instructions)
        instructionButton.setOnClickListener {
            val i = Intent(this, InstructionActivity::class.java)
            startActivity(i)
        }

        statButton = findViewById(R.id.button_statistics)
        statButton.setOnClickListener {
            val i = Intent(this, StatisticsActivity::class.java)
            startActivity(i)
        }

        spotify = Spotify(this)


        artistImg = findViewById(R.id.artist_image)
        artistImg.setImageResource(R.mipmap.ic_launcher)
        artistName = findViewById(R.id.artist_name)
        // TODO: update with API fetched artist
        queue = Volley.newRequestQueue(this)
        spotifyCalls()

    }


    private fun spotifyCalls(){
        queue.add(spotify.tokenRequest { response ->
            val token = response.getString("access_token")
            val str = "ABCDEFGHIJKLMNOPQRSTUV"
            val char = str.random()
            queue.add(spotify.randomArtistRequest(token, char){
                    response ->
                val randInt = Random(System.currentTimeMillis()).nextInt(0, 20)
                val id = response.getJSONObject("artists").getJSONArray("items").getJSONObject(randInt).getString("id")
                queue.add(spotify.artistRequest(token, id){
                        response ->
                    artistName.text = response.getString("name")
                    val imgURL = response.getJSONArray("images").getJSONObject(0).getString("url")
                    Picasso.get()
                        .load(imgURL)
                        .into(artistImg)
                })
            })
        })
    }
}
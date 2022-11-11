package com.sfu362group2.musicistrivial.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.sfu362group2.musicistrivial.R
import com.sfu362group2.musicistrivial.api.Spotify
import com.sfu362group2.musicistrivial.view_models.MainViewModel
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var artistImg: ImageView
    private lateinit var artistName: TextView
    private lateinit var playButton: Button
    private lateinit var instructionButton: Button
    private lateinit var statButton: Button
    private lateinit var loginButton: Button
    private lateinit var queue: RequestQueue
    private lateinit var spotify: Spotify
    private lateinit var viewModel: MainViewModel

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

        artistImg = findViewById(R.id.artist_image)
        artistImg.setImageResource(R.mipmap.ic_launcher)
        artistName = findViewById(R.id.artist_name)

        spotify = Spotify(this)
        queue = Volley.newRequestQueue(this)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.artistName.observe(this) {
            artistName.text = viewModel.artistName.value
        }
        viewModel.artistImgUrl.observe(this) {
            Picasso.get()
                .load(viewModel.artistImgUrl.value)
                .into(artistImg)
        }

    }

    override fun onResume() {
        super.onResume()
        if (viewModel.date.value != LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)) {
            viewModel.setDate()
            viewModel.spotifyCalls(spotify, queue)
        }
    }
}
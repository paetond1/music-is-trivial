package com.sfu362group2.musicistrivial.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.sfu362group2.musicistrivial.R

class MainActivity : AppCompatActivity() {

    private lateinit var artistImg: ImageView
    private lateinit var artistName: TextView
    private lateinit var playButton: Button
    private lateinit var instructionButton: Button
    private lateinit var statButton: Button
    private lateinit var loginButton: Button

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
        // TODO: update with API fetched artist

        artistName = findViewById(R.id.artist_name)
        // TODO: update with API fetched artist
    }
}
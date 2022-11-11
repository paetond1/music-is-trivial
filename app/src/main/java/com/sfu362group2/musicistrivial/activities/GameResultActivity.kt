package com.sfu362group2.musicistrivial.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.sfu362group2.musicistrivial.R

class GameResultActivity : AppCompatActivity() {

    private lateinit var artistName: TextView
    private lateinit var homeButton: Button
    private lateinit var shareToFbButton: Button

    // TODO: The songs displayed will take an array of top 5 songs from API
    //  and display using a ListView. Will need to implement UI logic to
    //  change colours based on guesses

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)

        artistName = findViewById(R.id.artist_name)

        homeButton = findViewById(R.id.button_to_home)
        homeButton.setOnClickListener {
            finish()
        }

        shareToFbButton = findViewById(R.id.button_share_to_Facebook)

    }
}
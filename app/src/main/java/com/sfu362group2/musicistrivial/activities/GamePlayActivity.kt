package com.sfu362group2.musicistrivial.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.sfu362group2.musicistrivial.R

class GamePlayActivity : AppCompatActivity() {

    private lateinit var artistName: TextView
    private lateinit var clearButton: Button
    private lateinit var submitButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)

        artistName = findViewById(R.id.artist_name)
        // TODO: Set Artist's name using viewModel

        clearButton = findViewById(R.id.button_clear_selections)
        clearButton.setOnClickListener {
            // TODO
        }

        submitButton = findViewById(R.id.button_submit)
        submitButton.setOnClickListener {
            // TODO: game logic and add to Intent
            val i = Intent(this, GameResultActivity::class.java)
            startActivity(i)
            finish()
        }

        // TODO: this part will be converted to ListView.
        //  Please make sure your API call results can be put in an Array.
        var testSong_1 = findViewById<TextView>(R.id.test_song_1)
        var testSong_2 = findViewById<TextView>(R.id.test_song_2)
        var testSong_3 = findViewById<TextView>(R.id.test_song_3)
        // You should be able to set onClick
    }
}
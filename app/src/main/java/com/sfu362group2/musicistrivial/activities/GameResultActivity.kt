package com.sfu362group2.musicistrivial.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.sfu362group2.musicistrivial.R

class GameResultActivity : AppCompatActivity() {

    private lateinit var artistName: TextView
    private lateinit var gameResult: TextView
    private lateinit var song1: TextView
    private lateinit var song2: TextView
    private lateinit var song3: TextView
    private lateinit var song4: TextView
    private lateinit var song5: TextView
    private lateinit var homeButton: Button
    private lateinit var shareToFbButton: Button
    private lateinit var inBundle: Bundle
    private lateinit var detailedScore: FloatArray
    private lateinit var correctSongs: ArrayList<String>

    // TODO: The songs displayed will take an array of top 5 songs from API
    //  and display using a ListView. Will need to implement UI logic to
    //  change colours based on guesses

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)
        initTextViews()
        if (intent.extras != null){
            inBundle = intent.extras!!
            setText()
        }

        homeButton = findViewById(R.id.button_to_home)
        homeButton.setOnClickListener {
            finish()
        }

        shareToFbButton = findViewById(R.id.button_share_to_Facebook)

    }

    private fun initTextViews(){
        artistName = findViewById(R.id.artist_name)
        gameResult = findViewById(R.id.game_result_message)
        song1 = findViewById(R.id.rank_1_song)
        song2 = findViewById(R.id.rank_2_song)
        song3 = findViewById(R.id.rank_3_song)
        song4 = findViewById(R.id.rank_4_song)
        song5 = findViewById(R.id.rank_5_song)
    }

    private fun setText(){
        artistName.text = inBundle.getString(getString(R.string.bund_key_artist_name), "")
        detailedScore = inBundle.getFloatArray(getString(R.string.bund_key_detailed_score))!!
        correctSongs = inBundle.getStringArrayList(getString(R.string.bund_key_correct_songs))!!

        song1.text = correctSongs[0]
        song2.text = correctSongs[1]
        song3.text = correctSongs[2]
        song4.text = correctSongs[3]
        song5.text = correctSongs[4]

        gameResult.text = getString(R.string.result_message, detailedScore.sum())
    }
}
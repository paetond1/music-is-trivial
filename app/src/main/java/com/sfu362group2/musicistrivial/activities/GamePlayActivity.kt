package com.sfu362group2.musicistrivial.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sfu362group2.musicistrivial.R
import com.sfu362group2.musicistrivial.view_models.GamePlayViewModel

class GamePlayActivity : AppCompatActivity() {

    private lateinit var artistName: TextView
    private lateinit var clearButton: Button
    private lateinit var submitButton: Button
    private lateinit var inBundle: Bundle
    private lateinit var viewModel: GamePlayViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)
        inBundle = intent.extras!!
        artistName = findViewById(R.id.artist_name)

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
        //  Please make sure your API call results can be put in an Array. - DONE -call viewModel.game.value.getSongOptions() for the arraylist
        val testSong1 = findViewById<TextView>(R.id.test_song_1)
        val testSong2 = findViewById<TextView>(R.id.test_song_2)
        val testSong3 = findViewById<TextView>(R.id.test_song_3)

        initViewModel()
        artistName.text = viewModel.game.value?.getArtistName()
        testSong1.text = viewModel.game.value?.getSongOptions()?.get(0)
        testSong2.text = viewModel.game.value?.getSongOptions()?.get(1)
        testSong3.text = viewModel.game.value?.getSongOptions()?.get(2)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[GamePlayViewModel::class.java]
        val date = inBundle.getString(getString(R.string.bund_key_date))
        val artistName = inBundle.getString(getString(R.string.bund_key_artist_name))
        val topTracks = inBundle.getStringArrayList(getString(R.string.bund_key_all_songs))
        viewModel.setGame(date!!, artistName!!, topTracks!!)

        for (i in 0..4){
            println("DEBUG: ${viewModel.game.value?.getCorrectSongs()?.get(i)}")
        }
    }
}
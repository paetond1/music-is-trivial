package com.sfu362group2.musicistrivial.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sfu362group2.musicistrivial.R
import com.sfu362group2.musicistrivial.adapters.SongListAdapter
import com.sfu362group2.musicistrivial.view_models.GamePlayViewModel

class GamePlayActivity : AppCompatActivity() {

    private lateinit var artistName: TextView
    private lateinit var shuffledTracks: ArrayList<Song>
    private var rankCounter: Int = 0
    private lateinit var listView: ListView
    private lateinit var listViewAdapter: SongListAdapter
    private lateinit var clearButton: Button
    private lateinit var submitButton: Button
    private lateinit var inBundle: Bundle
    private lateinit var viewModel: GamePlayViewModel

    companion object {
        val NUM_OF_SONGS = 8
        val MAX_CHOSEN_SONGS = 5
    }

    inner class Song(val song_title: String, var input_rank: Int = 0, val actual_rank: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)
        inBundle = intent.extras!!
        artistName = findViewById(R.id.artist_name)

        clearButton = findViewById(R.id.button_clear_selections)
        clearButton.setOnClickListener { onClick(it) }

        submitButton = findViewById(R.id.button_submit)
        submitButton.setOnClickListener { onClick(it) }

        initViewModel()
        artistName.text = viewModel.game.value?.getArtistName()

        listView = findViewById(R.id.song_listview)
        listViewAdapter = SongListAdapter(this, shuffledTracks)
        listView.adapter = listViewAdapter
        listView.setOnItemClickListener { parent, view, position, id -> songOnClick(position) }

        viewModel.shuffledSongs.observe(this) {
            this.shuffledTracks = it
            listViewAdapter.replaceList(it)
            listViewAdapter.notifyDataSetChanged()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[GamePlayViewModel::class.java]
        val date = inBundle.getString(getString(R.string.bund_key_date))
        val artistName = inBundle.getString(getString(R.string.bund_key_artist_name))
        val topTracks = inBundle.getStringArrayList(getString(R.string.bund_key_all_songs))
        // Shuffle the tracks in an ArrayList<Song>
        shuffledTracks = ArrayList(NUM_OF_SONGS)
        if (topTracks != null) {
            for (rank in 0 until NUM_OF_SONGS) {
                shuffledTracks.add(Song(topTracks[rank], 0, rank))
            }
            shuffledTracks.shuffle()
        }

        viewModel.setGame(date!!, artistName!!, topTracks!!, shuffledTracks)

        for (i in 0..4){
            println("DEBUG: ${viewModel.game.value?.getCorrectSongs()?.get(i)}")
        }
    }

    private fun onClick(view: View) {
        when (view) {
            submitButton -> {
                // TODO: game logic and add to Intent
                // TODO: remember to check for duplicated rank! Currently bugged
                val i = Intent(this, GameResultActivity::class.java)
                startActivity(i)
                finish()
            }
            clearButton -> {
                // Set all input_rank to 0
                for (song in shuffledTracks) {
                    song.input_rank = 0
                }
                updateAndNotifyAdapter(shuffledTracks)
            }

        }
    }

    // Logic for ranking the list
    private fun songOnClick(position: Int) {
        if (shuffledTracks[position].input_rank == 0 && rankCounter < MAX_CHOSEN_SONGS) {
            shuffledTracks[position].input_rank = ++rankCounter
        } else if (shuffledTracks[position].input_rank != 0) {
            shuffledTracks[position].input_rank = 0
            --rankCounter
        }
        updateAndNotifyAdapter(shuffledTracks)
    }

    // Update ArrayList in ListView Adapter and notify change
    private fun updateAndNotifyAdapter(songList: ArrayList<Song>) {
        viewModel.shuffledSongs.value = songList
        listViewAdapter.replaceList(songList)
        listViewAdapter.notifyDataSetChanged()
    }
}
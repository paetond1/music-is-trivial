package com.sfu362group2.musicistrivial.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sfu362group2.musicistrivial.MusicTriviaApplication
import com.sfu362group2.musicistrivial.R
import com.sfu362group2.musicistrivial.adapters.SongListAdapter
import com.sfu362group2.musicistrivial.database.GameHistory
import com.sfu362group2.musicistrivial.game_logic.Game
import com.sfu362group2.musicistrivial.view_models.GameHistoryViewModel
import com.sfu362group2.musicistrivial.view_models.GameHistoryViewModelFactory
import com.sfu362group2.musicistrivial.view_models.GamePlayViewModel

private const val TAG = "DEBUG: GamePlayActivity - "

class GamePlayActivity : AppCompatActivity() {

    private lateinit var artistName: TextView
    private lateinit var listView: ListView
    private lateinit var listViewAdapter: SongListAdapter
    private lateinit var clearButton: Button
    private lateinit var submitButton: Button
    private lateinit var inBundle: Bundle
    private lateinit var viewModel: GamePlayViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    // use this to access the database
    private val gameHistoryViewModel: GameHistoryViewModel by viewModels {
        GameHistoryViewModelFactory((application as MusicTriviaApplication).gameHistoryRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)
        inBundle = intent.extras!!

        // init buttons
        clearButton = findViewById(R.id.button_clear_selections)
        clearButton.setOnClickListener { onClick(it) }
        submitButton = findViewById(R.id.button_submit)
        submitButton.setOnClickListener { onClick(it) }

        // init views
        initViewModel()
        initSharedPref()
        artistName = findViewById(R.id.artist_name)
        artistName.text = viewModel.game.value?.getArtistName()
        listView = findViewById(R.id.song_listview)
        listViewAdapter = SongListAdapter(this, viewModel.game.value!!.getSongOptions())
        listView.adapter = listViewAdapter
        listView.setOnItemClickListener { parent, view, position, id -> songOnClick(position) }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[GamePlayViewModel::class.java]
        val date = inBundle.getLong(getString(R.string.bund_key_date))
        val artistName = inBundle.getString(getString(R.string.bund_key_artist_name))
        val topTracks = inBundle.getStringArrayList(getString(R.string.bund_key_all_songs))
        viewModel.setGame(date, artistName!!, topTracks!!)
        viewModel.shuffledSongs.observe(this) {
            listViewAdapter.replaceList(it)
            listViewAdapter.notifyDataSetChanged()
        }
        if (viewModel.rankCounter == 0) {
            clearButton.setBackgroundColor(getColor(R.color.greyed_out))
        } else {
            clearButton.setBackgroundColor(getColor(R.color.green_result))
        }

    }

    private fun initSharedPref() {
        sharedPreferences = this.getSharedPreferences(
            getString(R.string.SHARED_PREF_KEY),
            Context.MODE_PRIVATE
        )
        editor = sharedPreferences.edit()
    }

    private fun onClick(view: View) {
        when (view) {
            submitButton -> {
                val detailedScore = viewModel.submitSongs()
                if (detailedScore != null) {

                    val entry = GameHistory()
                    entry.date = viewModel.game.value!!.getDate()
                    entry.score = detailedScore.sum()
                    Log.i(
                        TAG,
                        "Detailed Score: ${detailedScore[0]} ${detailedScore[1]} ${detailedScore[2]} ${detailedScore[3]} ${detailedScore[4]}"
                    )
                    gameHistoryViewModel.insertEntry(entry)
                    editor.apply {
                        putLong(getString(R.string.LAST_PLAYED_DATE_KEY), entry.date!!)
                        putFloat(getString(R.string.LAST_SCORE_KEY), entry.score)
                    }.apply()
                    val outBundle = Bundle()
                    outBundle.putString(
                        getString(R.string.bund_key_artist_name),
                        viewModel.game.value?.getArtistName()
                    )
                    outBundle.putFloatArray(
                        getString(R.string.bund_key_detailed_score),
                        detailedScore.toFloatArray()
                    )
                    outBundle.putStringArrayList(
                        getString(R.string.bund_key_correct_songs),
                        viewModel.game.value!!.getCorrectSongs()
                    )

                    val i = Intent(this, GameResultActivity::class.java)
                    i.putExtras(outBundle)
                    startActivity(i)
                    finish()
                } else {
                    alertNotEnoughSelections()
                }
            }
            clearButton -> {
                // Set all input_rank to 0
                viewModel.clearRanks()
                updateAndNotifyAdapter(viewModel.shuffledSongs.value as ArrayList<Game.Song>)
                clearButton.setBackgroundColor(getColor(R.color.greyed_out))
            }

        }
    }

    // Logic for ranking the list
    private fun songOnClick(position: Int) {
        viewModel.rankSong(position)
        updateAndNotifyAdapter(viewModel.shuffledSongs.value!!)
        if (viewModel.rankCounter == 0) {
            clearButton.setBackgroundColor(getColor(R.color.greyed_out))
        } else {
            clearButton.setBackgroundColor(getColor(R.color.green_result))
        }
    }

    // Update ArrayList in ListView Adapter and notify change
    private fun updateAndNotifyAdapter(songList: ArrayList<Game.Song>) {
        viewModel.shuffledSongs.value = songList
        listViewAdapter.replaceList(songList)
        listViewAdapter.notifyDataSetChanged()
    }

    private fun alertNotEnoughSelections() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
        builder.setTitle(getString(R.string.alert_submission_title))
        builder.setMessage(getString(R.string.alert_submission_message))
        builder.setPositiveButton(getString(R.string.alert_positive)) { _, _ ->
        }
        builder.show()
    }

}
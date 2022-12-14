package com.sfu362group2.musicistrivial.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.sfu362group2.musicistrivial.MusicTriviaApplication
import com.sfu362group2.musicistrivial.R
import com.sfu362group2.musicistrivial.api.Spotify
import com.sfu362group2.musicistrivial.view_models.GameHistoryViewModel
import com.sfu362group2.musicistrivial.view_models.GameHistoryViewModelFactory
import com.sfu362group2.musicistrivial.view_models.MainViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicBoolean

private const val TAG = "DEBUG: MainActivity - "

class MainActivity : AppCompatActivity(), SplashScreen.KeepOnScreenCondition {

    private lateinit var splashScreen: SplashScreen
    private lateinit var artistImg: ImageView
    private lateinit var artistName: TextView
    private lateinit var playButton: Button
    private lateinit var instructionButton: Button
    private lateinit var statButton: Button
    private lateinit var loginButton: Button
    private lateinit var queue: RequestQueue
    private lateinit var spotify: Spotify
    private lateinit var viewModel: MainViewModel
    private lateinit var todaysScoreMessage: TextView
    private lateinit var currentGameStreak: TextView

    private lateinit var sharedPreferences: SharedPreferences
    private val imgLoaded = AtomicBoolean()

    // use this to access the database
    private val gameHistoryViewModel: GameHistoryViewModel by viewModels {
        GameHistoryViewModelFactory((application as MusicTriviaApplication).gameHistoryRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initViewModel()
        initSharedPref()
        spotify = Spotify(this)
        queue = Volley.newRequestQueue(this)


        // Do not move installSplashScreen(). Must be before super.onCreate() and setContentView()
        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
//        artistImg.setImageResource(R.mipmap.ic_launcher)
        artistName = findViewById(R.id.artist_name)
        currentGameStreak = findViewById(R.id.game_streak_message)
        initStreak()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.date.value != LocalDate.now().toEpochDay()) {
            viewModel.spotifyCalls(spotify, queue)
        }
        initPlayButton()
        initScore()
    }

    private fun gameBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString(getString(R.string.bund_key_artist_name), viewModel.artistName.value)
        bundle.putString(getString(R.string.bund_key_artist_id), viewModel.artistId.value)
        bundle.putLong(getString(R.string.bund_key_date), viewModel.date.value!!)
        bundle.putStringArrayList(
            getString(R.string.bund_key_all_songs),
            viewModel.allSongsInOrder.value
        )
        return bundle
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.artistName.observe(this) {
            artistName.text = it
            Log.i(TAG, "Observing artists name: ${it}")
        }
        viewModel.artistImgUrl.observe(this) {
            Log.i(TAG, "Observing artists url: ${it}")
            val x = Picasso.get()
                .load(viewModel.artistImgUrl.value)
                .into(artistImg, object: Callback.EmptyCallback() {
                    override fun onSuccess(){
                        Log.i(TAG, "Artist Image Loaded successfully")
                        imgLoaded.set(true)
                    }
                    override fun onError(e: Exception?) {
                        super.onError(e)
                        Log.e(TAG, "Error loading photo ${e?.message}")
                        imgLoaded.set(true)
                        alertNetworkError()
                    }
                })
            Log.i(TAG, "x is $x")
        }
        viewModel.isRequestErrors.observe(this){
            if (it){
                alertNetworkError()
            }
        }
    }

    private fun initPlayButton() {
        playButton = findViewById(R.id.button_play)
        if (sharedPreferences.getLong(
                getString(R.string.LAST_PLAYED_DATE_KEY),
                -1L
            ) == viewModel.date.value
        ) {
            playButton.setBackgroundColor(getColor(R.color.greyed_out))
        } else {
            playButton.setBackgroundColor(getColor(R.color.light_purple))
        }
        playButton.setOnClickListener {
            val lastPlayedDate =
                sharedPreferences.getLong(getString(R.string.LAST_PLAYED_DATE_KEY), -1L)
            if (lastPlayedDate != -1L && lastPlayedDate == viewModel.date.value!!) {
                alertAlreadyPlayed()
            }
            else if(viewModel.isRequestErrors.value!!){
                alertNetworkError()
            }
            else {
                val i = Intent(this, GamePlayActivity::class.java)
                i.putExtras(gameBundle())
                startActivity(i)
            }
        }
    }

    private fun initSharedPref() {
        sharedPreferences = this.getSharedPreferences(
            getString(R.string.SHARED_PREF_KEY),
            Context.MODE_PRIVATE
        )
    }

    private fun initScore() {
        todaysScoreMessage = findViewById(R.id.todays_score_message)
        if (sharedPreferences.getLong(
                getString(R.string.LAST_PLAYED_DATE_KEY),
                -1L
            ) == viewModel.date.value
        ) {
            todaysScoreMessage.text = getString(
                R.string.result_message,
                sharedPreferences.getFloat(getString(R.string.LAST_SCORE_KEY), -1F)
            )
            todaysScoreMessage.visibility = View.VISIBLE
        } else {
            todaysScoreMessage.visibility = View.GONE
        }
    }

    override fun shouldKeepOnScreen(): Boolean {
        // TODO: fetch Spotify API before loading
        Log.i(TAG, "Calling shouldKeepOnScreen")
        return if (viewModel.isRequestErrors.value!!){
            false
        } else {
            !imgLoaded.get()
        }

    }

    private fun alertAlreadyPlayed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert_play_title))
        builder.setMessage(getString(R.string.alert_play_message))
        builder.setPositiveButton(getString(R.string.alert_positive)) { _, _ ->
        }
        builder.show()
    }

    private fun alertNetworkError(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert_network_title))
        builder.setMessage(getString(R.string.alert_network_message))
        builder.setPositiveButton(getString(R.string.alert_positive)) { _, _ ->
        }
        builder.show()
    }

    private fun initStreak(){
        gameHistoryViewModel.currentStreakLiveData.observe(this) {
            val lastPlayedDate = sharedPreferences.getLong(getString(R.string.LAST_PLAYED_DATE_KEY), -1L)
            val isStreakActive = (it != null) && (lastPlayedDate >= viewModel.date.value!! - 1)
            val streak = if (isStreakActive) it else 0
            currentGameStreak.text = getString(R.string.streak_message, streak)
        }
    }
}
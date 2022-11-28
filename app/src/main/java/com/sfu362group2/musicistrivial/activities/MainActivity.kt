package com.sfu362group2.musicistrivial.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.sfu362group2.musicistrivial.R
import com.sfu362group2.musicistrivial.api.Spotify
import com.sfu362group2.musicistrivial.view_models.MainViewModel
import com.squareup.picasso.Picasso
import java.time.LocalDate

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

    override fun onCreate(savedInstanceState: Bundle?) {

        initViewModel()
        spotify = Spotify(this)
        queue = Volley.newRequestQueue(this)
//        if (viewModel.date.value != LocalDate.now().toEpochDay()) {
//            viewModel.spotifyCalls(spotify, queue)
//        }

        // Do not move installSplashScreen(). Must be before super.onCreate() and setContentView()
        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todaysScoreMessage = findViewById(R.id.todays_score_message)
        // TODO: Update the message based on whether the user has played already today
        // todaysScoreMessage.text = ""

        playButton = findViewById(R.id.button_play)
        // TODO : Render streak
        // TODO : Change Button's background color to R.colors.greyed_out if already played today
        playButton.setOnClickListener {
            // TODO : Add logic to check that the day has not been played already

            val i = Intent(this, GamePlayActivity::class.java)
            i.putExtras(gameBundle())
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
//        artistImg.setImageResource(R.mipmap.ic_launcher)
        artistName = findViewById(R.id.artist_name)

//        initViewModel()
//        spotify = Spotify(this)
//        queue = Volley.newRequestQueue(this)

    }

    override fun onResume() {
        super.onResume()
        if (viewModel.date.value != LocalDate.now().toEpochDay()) {
            viewModel.spotifyCalls(spotify, queue)
        }
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
            artistName.text = viewModel.artistName.value
        }
        viewModel.artistImgUrl.observe(this) {
            Picasso.get()
                .load(viewModel.artistImgUrl.value)
                .into(artistImg)
        }
    }

    override fun shouldKeepOnScreen(): Boolean {
        // TODO: fetch Spotify API before loading
        return viewModel.artistImgUrl.value == null

    }
}
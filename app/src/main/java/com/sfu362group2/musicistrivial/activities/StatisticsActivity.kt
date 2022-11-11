package com.sfu362group2.musicistrivial.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.sfu362group2.musicistrivial.R

class StatisticsActivity : AppCompatActivity() {

    private lateinit var homeButton: Button

    // update the statistics using viewModel
    private lateinit var totalGamesStat: TextView
    private lateinit var totalScoreStat: TextView
    private lateinit var avgScoreStat: TextView
    private lateinit var perfectGamesStat: TextView
    private lateinit var longestStreakStat: TextView
    private lateinit var zeroScoreGamesStat: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        homeButton = findViewById(R.id.button_to_home)
        homeButton.setOnClickListener {
            finish()
        }
        // TODO: Programmatically update these with Database viewModel
        totalGamesStat = findViewById(R.id.total_games_played_stat)
        totalScoreStat = findViewById(R.id.total_score_stat)
        avgScoreStat = findViewById(R.id.average_score_stat)
        perfectGamesStat = findViewById(R.id.perfect_games_stat)
        longestStreakStat = findViewById(R.id.longest_streak_stat)
        zeroScoreGamesStat = findViewById(R.id.zero_score_games_stat)

    }
}
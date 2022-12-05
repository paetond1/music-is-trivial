package com.sfu362group2.musicistrivial.activities


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sfu362group2.musicistrivial.MusicTriviaApplication
import com.sfu362group2.musicistrivial.R
import com.sfu362group2.musicistrivial.view_models.GameHistoryViewModel
import com.sfu362group2.musicistrivial.view_models.GameHistoryViewModelFactory
import java.time.LocalDate


class StatisticsActivity : AppCompatActivity() {

    private lateinit var homeButton: Button

    // update the statistics using viewModel
    private lateinit var totalGamesStat: TextView
    private lateinit var totalScoreStat: TextView
    private lateinit var avgScoreStat: TextView
    private lateinit var perfectGamesStat: TextView
    private lateinit var longestStreakStat: TextView
    private lateinit var zeroScoreGamesStat: TextView
    private lateinit var currentStreakStat: TextView
    private lateinit var sharedPreferences: SharedPreferences

    // use this to access the database
    private val gameHistoryViewModel: GameHistoryViewModel by viewModels {
        GameHistoryViewModelFactory((application as MusicTriviaApplication).gameHistoryRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        initSharedPref()

        homeButton = findViewById(R.id.button_to_home)
        homeButton.setOnClickListener {
            finish()
        }
        bindUIComponents()
        renderTotalScore()
        renderTotalGamesPlayed()
        renderAvgScoreStat()
        renderLongestStreak()
        renderZeroScoreGames()
        renderPerfectScore()
        renderCurrentStreak()
    }

    private fun renderTotalScore() {
        gameHistoryViewModel.totalScoreLiveData.observe(this) {
            totalScoreStat.text = it?.toString() ?: "0"
        }
    }
    private fun renderTotalGamesPlayed() {
        gameHistoryViewModel.totalGamesPlayedLiveData.observe(this) {
            totalGamesStat.text = (it.toString())
        }
    }

    private fun renderAvgScoreStat() {
        gameHistoryViewModel.avgScoreLiveData.observe(this) {
            avgScoreStat.text = it?.toString() ?: "0"
        }
    }

    private fun renderLongestStreak() {
        gameHistoryViewModel.longestStreakLiveData.observe(this) {
            longestStreakStat.text = it?.toString() ?: "0"
        }
    }

    private fun renderZeroScoreGames() {
        gameHistoryViewModel.zeroScoredGamesLiveData.observe(this) {
            zeroScoreGamesStat.text = it.toString()
        }
    }

    private fun renderPerfectScore() {
        gameHistoryViewModel.perfectScoredGamesLiveData.observe(this) {
            perfectGamesStat.text = it.toString()
        }
    }

    private fun renderCurrentStreak() {
        gameHistoryViewModel.currentStreakLiveData.observe(this) {
            val lastPlayedDate = sharedPreferences.getLong(getString(R.string.LAST_PLAYED_DATE_KEY), -1L)
            val isStreakActive = (it != null) && (lastPlayedDate >= LocalDate.now().toEpochDay() - 1)
            val streak = if (isStreakActive) it else 0
            currentStreakStat.text = streak.toString()
        }
    }

    private fun bindUIComponents() {
        totalGamesStat = findViewById(R.id.total_games_played_stat)
        totalScoreStat = findViewById(R.id.total_score_stat)
        avgScoreStat = findViewById(R.id.average_score_stat)
        perfectGamesStat = findViewById(R.id.perfect_games_stat)
        longestStreakStat = findViewById(R.id.longest_streak_stat)
        zeroScoreGamesStat = findViewById(R.id.zero_score_games_stat)
        currentStreakStat = findViewById(R.id.currentStreak)
    }

    private fun initSharedPref(){
            sharedPreferences = this.getSharedPreferences(
                getString(R.string.SHARED_PREF_KEY),
                Context.MODE_PRIVATE
            )
        }

}
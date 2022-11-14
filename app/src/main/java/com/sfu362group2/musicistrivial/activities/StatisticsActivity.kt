package com.sfu362group2.musicistrivial.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.sfu362group2.musicistrivial.R
import com.sfu362group2.musicistrivial.adapters.GameHistoryAdapter
import com.sfu362group2.musicistrivial.database.GameHistory
import com.sfu362group2.musicistrivial.database.GameHistoryDb
import com.sfu362group2.musicistrivial.database.GameHistoryDbDao
import com.sfu362group2.musicistrivial.database.GameHistoryRepository
import com.sfu362group2.musicistrivial.view_models.GameHistoryViewModel
import java.util.*
import kotlin.collections.ArrayList

class StatisticsActivity : AppCompatActivity() {

    private lateinit var homeButton: Button

    // update the statistics using viewModel
    private lateinit var totalGamesStat: TextView
    private lateinit var totalScoreStat: TextView
    private lateinit var avgScoreStat: TextView
    private lateinit var perfectGamesStat: TextView
    private lateinit var longestStreakStat: TextView
    private lateinit var zeroScoreGamesStat: TextView
    private lateinit var winButton: Button
    private lateinit var looseButton: Button

    private lateinit var gameHistoryDatabase: GameHistoryDb
    private lateinit var gameHistoryDatabaseDao: GameHistoryDbDao
    private lateinit var gameHistoryRepository: GameHistoryRepository
    private lateinit var gameHistoryViewModel: GameHistoryViewModel
    private lateinit var gameHistoryViewModelFactory: GameHistoryViewModel.GameHistoryViewModelFactory
    private lateinit var gameHistoryAdapter: GameHistoryAdapter
    private lateinit var gameHistoryArrayList: ArrayList<GameHistory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        homeButton = findViewById(R.id.button_to_home)
        homeButton.setOnClickListener {
            finish()
        }
        // TODO: Programmatically update these with Database viewModel
        bindUIComponents()
        initDatabase()


        winButton.setOnClickListener {
            var entry = GameHistory()
            entry.date = getCurrentDateTimeAsLong()
            entry.score = 2.5f
            var count = 0
            gameHistoryViewModel.getCurrentStreak().observe(this) {
                if (it != null) {
                    entry.streak = it + 1
                    if(count < 1) {
                        gameHistoryViewModel.insertEntry(entry)
                        count++
                    }
                }
                else {
                    entry.streak = 1
                    if(count < 1) {
                        gameHistoryViewModel.insertEntry(entry)
                        count++
                    }
                }
            }
        }

        looseButton.setOnClickListener{
            var entry = GameHistory()
            entry.date = getCurrentDateTimeAsLong()
            entry.score = 0.0f
            entry.streak = 0
            gameHistoryViewModel.insertEntry(entry)
        }

        renderTotalScore()
        renderTotalGamesPlayed()
        renderAvgScoreStat()
        renderLongestStreak()
        renderZeroScoreGames()
    }

    private fun renderTotalScore() {
        gameHistoryViewModel.getTotalScore().observe(this) {
            totalScoreStat.text = (it.toString())
        }
    }
    private fun renderTotalGamesPlayed() {
        gameHistoryViewModel.getTotalGamesPlayed().observe(this) {
            totalGamesStat.text = (it.toString())
        }
    }

    private fun renderAvgScoreStat() {
        gameHistoryViewModel.getAvgScore().observe(this) {
            avgScoreStat.text = it.toString()
        }
    }

    private fun renderLongestStreak() {
        gameHistoryViewModel.getLongestStreak().observe(this) {
            longestStreakStat.text = it.toString()
        }
    }

    private fun renderZeroScoreGames() {
        gameHistoryViewModel.getTotalZeroScoreGames().observe(this) {
            zeroScoreGamesStat.text = it.toString()
        }
    }


    private fun bindUIComponents() {
        totalGamesStat = findViewById(R.id.total_games_played_stat)
        totalScoreStat = findViewById(R.id.total_score_stat)
        avgScoreStat = findViewById(R.id.average_score_stat)
        perfectGamesStat = findViewById(R.id.perfect_games_stat)
        longestStreakStat = findViewById(R.id.longest_streak_stat)
        zeroScoreGamesStat = findViewById(R.id.zero_score_games_stat)
        winButton = findViewById(R.id.win_button)
        looseButton = findViewById(R.id.loose_button)

    }

    private fun initDatabase() {
        gameHistoryDatabase = GameHistoryDb.getInstance(this)
        gameHistoryDatabaseDao = gameHistoryDatabase.gameHistoryDbDao
        gameHistoryRepository = GameHistoryRepository(gameHistoryDatabaseDao)
        gameHistoryViewModelFactory = GameHistoryViewModel.GameHistoryViewModelFactory(gameHistoryRepository)
        gameHistoryViewModel = ViewModelProvider(this, gameHistoryViewModelFactory).get(GameHistoryViewModel::class.java)
        gameHistoryArrayList = ArrayList()
        gameHistoryAdapter = GameHistoryAdapter(this, gameHistoryArrayList)

        gameHistoryViewModel.allGameHistoryLiveData.observe(this) {
            gameHistoryAdapter.replaceList(it)
            gameHistoryAdapter.notifyDataSetChanged()
        }
    }

    private fun convertLongToDateTime(dateTimeAsLong: Long) : Date {
        return Date(dateTimeAsLong)
    }

    private fun getCurrentDateTimeAsLong() : Long {
        val dateTime: Date = Calendar.getInstance().time
        return dateTime.time
    }
}
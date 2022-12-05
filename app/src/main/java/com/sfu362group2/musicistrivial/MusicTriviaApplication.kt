package com.sfu362group2.musicistrivial

import android.app.Application
import com.sfu362group2.musicistrivial.database.GameHistoryDb
import com.sfu362group2.musicistrivial.database.GameHistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MusicTriviaApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    //database and repository created when they are needed
    private val gameHistoryDb by lazy {GameHistoryDb.getInstance(this, applicationScope)}
    val gameHistoryRepository by lazy {GameHistoryRepository(gameHistoryDb.gameHistoryDbDao())}

}
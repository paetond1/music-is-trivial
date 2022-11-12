package com.sfu362group2.musicistrivial.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
/*********Singleton database implementation***********/
@Database(entities = [GameHistory::class], version = 1)
abstract class GameHistoryDb: RoomDatabase() {
    abstract val gameHistoryDbDao: GameHistoryDbDao

    companion object {
        @Volatile
        private var INSTANCE: GameHistoryDb? = null

        fun getInstance(context: Context) : GameHistoryDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        GameHistoryDb::class.java, "history_DB").build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}
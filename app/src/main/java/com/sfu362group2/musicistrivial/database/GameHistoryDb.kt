package com.sfu362group2.musicistrivial.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/*********Singleton database implementation***********/
@Database(entities = [GameHistory::class], version = 1)
abstract class GameHistoryDb: RoomDatabase() {

    abstract fun gameHistoryDbDao() : GameHistoryDbDao

    companion object {
        @Volatile
        private var INSTANCE: GameHistoryDb? = null

        fun getInstance(context: Context, scope: CoroutineScope) : GameHistoryDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        GameHistoryDb::class.java, "history_DB").addCallback(GameHistoryCallback(scope)).build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

    //Will be called when Database is created, can populate with default values
    private class GameHistoryCallback(val scope: CoroutineScope): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { gameHistoryDb ->
                scope.launch {

                }
            }
        }
    }
}
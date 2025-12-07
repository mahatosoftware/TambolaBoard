package `in`.mahato.tambola.game

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CalledNumber::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calledNumberDao(): CalledNumberDao
}
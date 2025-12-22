package `in`.mahato.tambola.game

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CalledNumber::class, GameMetadata::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calledNumberDao(): CalledNumberDao
}
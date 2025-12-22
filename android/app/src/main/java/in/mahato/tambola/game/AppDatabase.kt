package `in`.mahato.tambola.game

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import `in`.mahato.tambola.rule.RuleDao
import `in`.mahato.tambola.rule.model.SavedRuleEntity

@Database(entities = [CalledNumber::class, GameMetadata::class, SavedRuleEntity::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calledNumberDao(): CalledNumberDao

    abstract fun ruleDao(): RuleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tambola_db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }

}
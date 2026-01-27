package `in`.mahato.tambola.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import `in`.mahato.tambola.game.dao.CalledNumberDao
import `in`.mahato.tambola.game.entity.CalledNumber
import `in`.mahato.tambola.game.entity.GameMetadata
import `in`.mahato.tambola.rule.dao.RuleDao
import `in`.mahato.tambola.rule.dao.WinningPrizeDao
import `in`.mahato.tambola.rule.entity.SavedRuleEntity
import `in`.mahato.tambola.rule.entity.WinningPrizeEntity
import `in`.mahato.tambola.game.dao.PlayerDao
import `in`.mahato.tambola.game.entity.PlayerEntity

@Database(entities = [CalledNumber::class, GameMetadata::class, SavedRuleEntity::class, WinningPrizeEntity::class, PlayerEntity::class], version = 8, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calledNumberDao(): CalledNumberDao
    abstract fun playerDao(): PlayerDao

    abstract fun ruleDao(): RuleDao
    abstract fun winningPrizeDao(): WinningPrizeDao


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
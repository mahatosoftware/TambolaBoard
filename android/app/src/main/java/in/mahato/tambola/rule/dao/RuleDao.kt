package `in`.mahato.tambola.rule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import `in`.mahato.tambola.rule.model.SavedRuleEntity

@Dao
interface RuleDao {

    @Query("SELECT * FROM saved_rules")
    suspend fun getAllSavedRules(): List<SavedRuleEntity>

    @Query("DELETE FROM saved_rules")
    suspend fun deleteAllRules()

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertRules(rules: List<SavedRuleEntity>)

    @Transaction
    suspend fun replaceRules(rules: List<SavedRuleEntity>) {
        deleteAllRules()
        insertRules(rules)
    }
}
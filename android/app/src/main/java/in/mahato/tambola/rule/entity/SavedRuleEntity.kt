package `in`.mahato.tambola.rule.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "saved_rules")
data class SavedRuleEntity(
    @PrimaryKey(autoGenerate = true) val dbId: Int = 0, // Room's internal ID
    val ruleId: Int,             // The ID from your TambolaRule object
    val ruleName: String,
    val percentage: Int,
    val quantity: Int,
    val amountPerItem: Int,
    val totalRuleAmount: Int
)

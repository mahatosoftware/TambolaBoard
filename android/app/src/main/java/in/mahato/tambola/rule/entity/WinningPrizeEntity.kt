package `in`.mahato.tambola.rule.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "winning_prizes")
data class WinningPrizeEntity(

    @PrimaryKey(autoGenerate = true)
    val prizeId: Int = 0,

    @Embedded
    val savedRule: SavedRuleEntity,

    val isClaimed: Boolean = false
)

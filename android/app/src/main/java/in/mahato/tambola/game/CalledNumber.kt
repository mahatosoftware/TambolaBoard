package `in`.mahato.tambola.game

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "called_numbers")
data class CalledNumber(
    @PrimaryKey val number: Int,
    val isLast: Boolean = false
)
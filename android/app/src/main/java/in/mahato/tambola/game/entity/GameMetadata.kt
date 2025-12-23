package `in`.mahato.tambola.game.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_metadata")
data class GameMetadata(
    @PrimaryKey val id: Int = 1,
    val gameId: String
)
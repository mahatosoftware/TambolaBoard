package `in`.mahato.tambola.game.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import `in`.mahato.tambola.game.entity.CalledNumber
import `in`.mahato.tambola.game.entity.GameMetadata

@Dao
interface CalledNumberDao {
    @Query("SELECT * FROM called_numbers")
    suspend fun getAll(): List<CalledNumber>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(number: CalledNumber)

    @Query("UPDATE called_numbers SET isLast = 0")
    suspend fun resetLast()

    @Query("DELETE FROM called_numbers")
    suspend fun clearNumbers()

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveGameMetadata(metadata: GameMetadata)

    @Query("SELECT gameId FROM game_metadata WHERE id = 1")
    suspend fun getSavedGameId(): String?

    @Query("DELETE FROM game_metadata")
    suspend fun clearMetadata()

    @Transaction
    suspend fun resetBoard() {
        clearNumbers()
        clearMetadata()
    }
}
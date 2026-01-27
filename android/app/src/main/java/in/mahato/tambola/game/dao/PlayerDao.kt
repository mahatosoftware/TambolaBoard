package `in`.mahato.tambola.game.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import `in`.mahato.tambola.game.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(players: List<PlayerEntity>)

    @Query("SELECT * FROM players WHERE gameId = :gameId")
    fun getPlayers(gameId: String): Flow<List<PlayerEntity>>
    
    @Query("SELECT * FROM players WHERE gameId = :gameId")
    suspend fun getPlayersList(gameId: String): List<PlayerEntity>

    @Query("DELETE FROM players WHERE gameId = :gameId")
    suspend fun deleteByGameId(gameId: String)
    
    @Query("DELETE FROM players")
    suspend fun deleteAll()
}

package `in`.mahato.tambola.rule.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import `in`.mahato.tambola.rule.entity.WinningPrizeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WinningPrizeDao {

    // --------------------------------------------------
    // LOAD FOR SCREEN (AUTO-OBSERVE)
    // --------------------------------------------------

    @Query("SELECT * FROM winning_prizes ORDER BY isClaimed ASC")
    fun getAllPrizes(): Flow<List<WinningPrizeEntity>>

    @Query("""
        SELECT * FROM winning_prizes
        WHERE isClaimed = 0
        ORDER BY prizeId ASC
    """)
    fun getUnclaimedPrizes(): Flow<List<WinningPrizeEntity>>

    // --------------------------------------------------
    // CLAIM / UNCLAIM
    // --------------------------------------------------

    @Query("""
        UPDATE winning_prizes
        SET isClaimed = 1
        WHERE prizeId = :prizeId
    """)
    suspend fun claimPrize(prizeId: Int)

    @Query("""
        UPDATE winning_prizes
        SET isClaimed = 0
        WHERE prizeId = :prizeId
    """)
    suspend fun unclaimPrize(prizeId: Int)

    // --------------------------------------------------
    // INSERT / UPDATE
    // --------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrize(prize: WinningPrizeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrizes(prizes: List<WinningPrizeEntity>)

    @Update
    suspend fun updatePrize(prize: WinningPrizeEntity)

    // --------------------------------------------------
    // CLEAR (NEW GAME)
    // --------------------------------------------------

    @Query("DELETE FROM winning_prizes")
    suspend fun clearAll()

    @Query("UPDATE winning_prizes SET winnerName = :name WHERE prizeId = :id")
    suspend fun updateWinnerName(id: Int, name: String)

    @Query("SELECT COUNT(*) FROM winning_prizes")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prize: WinningPrizeEntity)
}

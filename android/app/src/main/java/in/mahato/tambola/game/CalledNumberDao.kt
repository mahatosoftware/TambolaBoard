package `in`.mahato.tambola.game

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CalledNumberDao {
    @Query("SELECT * FROM called_numbers")
    suspend fun getAll(): List<CalledNumber>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(number: CalledNumber)

    @Query("UPDATE called_numbers SET isLast = 0")
    suspend fun resetLast()

    @Query("DELETE FROM called_numbers")
    suspend fun resetBoard()
}
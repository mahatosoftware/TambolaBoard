package `in`.mahato.tambola.winner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.room.Room
import `in`.mahato.tambola.db.AppDatabase
import `in`.mahato.tambola.ui.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
class ViewWinnersActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tambola_db"
        ).fallbackToDestructiveMigration().build()

        enableEdgeToEdge()

        setContent {
            AppTheme {
                Surface(color = Color.Transparent) {
                    // Collect the Flow from Room as a Compose State
                    val winnersList by db.winningPrizeDao()
                        .getAllPrizes()
                        .collectAsState(initial = emptyList())

                    // Sort the list based on totalRuleAmount
                    // remember(winnersList) ensures sorting only happens when the list changes
                    val sortedWinners = remember(winnersList) {
                        winnersList.sortedByDescending { it.savedRule.totalRuleAmount }
                    }
                    ViewWinnersScreen(   winners = sortedWinners,
                        onBack = { finish() })
                }
            }
        }
    }
}

package `in`.mahato.tambola.game

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import `in`.mahato.tambola.rule.RuleSelectionActivity
import `in`.mahato.tambola.ui.theme.AppTheme

class NewGameSetupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                NewGameSetupScreen(
                    onContinue = { gameId ->
                        // Proceed to Rule Selection, passing the Game ID
                        val intent = Intent(this, RuleSelectionActivity::class.java)
                        intent.putExtra("GAME_ID", gameId)
                        startActivity(intent)
                        finish() 
                    },
                    onBack = {
                        finish()
                    }
                )
            }
        }
    }
}
